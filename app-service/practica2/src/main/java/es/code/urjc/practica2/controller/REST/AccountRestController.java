package es.code.urjc.practica2.controller.REST;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import es.code.urjc.practica2.dto.AccountDto;
import es.code.urjc.practica2.dto.ListsDto;
import es.code.urjc.practica2.dto.ReviewDto;
import es.code.urjc.practica2.mapper.AccountMapper;
import es.code.urjc.practica2.mapper.ListsMapper;
import es.code.urjc.practica2.mapper.ReviewMapper;
import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Image;
import es.code.urjc.practica2.model.Lists;
import es.code.urjc.practica2.model.Review;
import es.code.urjc.practica2.service.AccountService;
import es.code.urjc.practica2.service.FilmographyService;
import es.code.urjc.practica2.service.ImageService;
import es.code.urjc.practica2.service.ListsService;
import es.code.urjc.practica2.service.ReviewService;

@RestController
@RequestMapping("/api")
public class AccountRestController {
    @Autowired
    private ListsService listsService;
    @Autowired
    private FilmographyService filmographyService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ImageService imageService;

    @Autowired 
    private AccountMapper accountMapper;
    @Autowired 
    private ReviewMapper reviewMapper;
    @Autowired 
    private ListsMapper listsMapper;

    // PROFILE

    @GetMapping("/profile")
    public ResponseEntity<AccountDto> getProfile(Principal principal){
        if(principal == null){
            return ResponseEntity.status(401).build();
        }
        Account account = accountService.findByEmail(principal.getName());
        return ResponseEntity.ok(accountMapper.toDTO(account));
    }

    @PutMapping("/profile/edit")
    public ResponseEntity<AccountDto> editProfile(@RequestParam String accountName, @RequestParam String accountEmail,@RequestParam LocalDate accountBirthDate, Principal principal ){
        if(principal == null){
            return ResponseEntity.status(401).build();
        }
        Account account = accountService.findByEmail(principal.getName());
        account.setAccountName(accountName);
        account.setAccountEmail(accountEmail);
        account.setAccountBirthDate(accountBirthDate);
        accountService.save(account);
        
        return ResponseEntity.ok(accountMapper.toDTO(account));
    }

    @PutMapping("/profile/avatar")
    public ResponseEntity<AccountDto> saveAvatar(@RequestParam Long imageId, Principal principal){
        if(principal == null){
            return ResponseEntity.status(401).build();
        }
        Account account = accountService.findByEmail(principal.getName());
        Image image = imageService.findById(imageId);
        if(image == null){
            return ResponseEntity.notFound().build();
        }
        account.setAccountAvatar(image);
        accountService.save(account);

        return ResponseEntity.ok(accountMapper.toDTO(account));
    }

    // REVIEWS

    @GetMapping("/myReviews")
    public ResponseEntity<List<ReviewDto>> getMyReviews(Principal principal){
        if(principal == null){
            return ResponseEntity.status(401).build();
        }
        Account account = accountService.findByEmail(principal.getName());
        boolean isAdmin = account.getAccountRole() == Account.Role.ADMIN;

        List<Review> reviews = isAdmin ? reviewService.findAll() : reviewService.findByAuthor(account);

        return ResponseEntity.ok(reviewMapper.toDTOs(reviews));
    }

    @PostMapping("/filmographies/{filmographyId}/reviews/new")
    public ResponseEntity<ReviewDto> createReview(@PathVariable Long filmographyId, @RequestBody ReviewDto reviewDto , Principal principal){
        if(principal == null){
            return ResponseEntity.status(401).build();
        }
        if(reviewDto.reviewStars() == null || reviewDto.reviewStars() <= 0){
            return ResponseEntity.badRequest().build();
        }
        Review saved = reviewService.save(reviewMapper.toDomain(reviewDto), filmographyId, principal.getName());

        return ResponseEntity.status(201).body(reviewMapper.toDTO(saved));

    }

    @PutMapping("/reviews/{reviewId}/edit")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long reviewId, @RequestParam Float reviewStars, @RequestParam String reviewDescription, Principal principal){
        if(principal == null){
            return ResponseEntity.status(401).build();
        }
        Review review = reviewService.findById(reviewId);
        if(review == null){
            return ResponseEntity.notFound().build();
        }
        boolean isAdmin = accountService.findByEmail(principal.getName()).getAccountRole() == Account.Role.ADMIN;
        boolean isOwner = review.getReviewAuthor().getAccountEmail().equals(principal.getName());

        if(!isAdmin && !isOwner){
            return ResponseEntity.status(403).build();
        }

        Long filmographyId = review.getFilmography().getFilmographyId();
        reviewService.update(reviewId, reviewStars, reviewDescription, filmographyId);

        return ResponseEntity.ok(reviewMapper.toDTO(reviewService.findById(reviewId)));

    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewDto> deleteReview(@PathVariable Long reviewId, Principal principal){
        if(principal == null){
            return ResponseEntity.status(401).build();
        }
        Review review = reviewService.findById(reviewId);
        if(review == null){
            return ResponseEntity.notFound().build();
        }
        boolean isAdmin = accountService.findByEmail(principal.getName()).getAccountRole() == Account.Role.ADMIN;
        boolean isOwner = review.getReviewAuthor().getAccountEmail().equals(principal.getName());

        if(!isAdmin && !isOwner){
            return ResponseEntity.status(403).build();
        }

        reviewService.delete(reviewId, review.getFilmography().getFilmographyId());

        return ResponseEntity.noContent().build();
    }

    // LISTS

    @GetMapping("/myLists")
    public ResponseEntity<List<ListsDto>> getMyLists(Principal principal){
        if(principal == null){
            return ResponseEntity.status(401).build();
        }

        Account account = accountService.findByEmail(principal.getName());
        boolean isAdmin = account.getAccountRole() == Account.Role.ADMIN;

        List<Lists> lists = isAdmin ? listsService.findAllSystemLists() : listsService.findByOwner(account);

        return ResponseEntity.ok(listsMapper.toDTOs(lists));
    }
    
    @PostMapping("/myLists/new")
    public ResponseEntity<ListsDto> createList(@RequestParam String listName, @RequestParam(required = false) String type, Principal principal){
        if(principal == null){
            return ResponseEntity.status(401).build();
        }

        Account account = accountService.findByEmail(principal.getName());
        boolean isAdmin = account.getAccountRole() == Account.Role.ADMIN;

        List<Lists> existing = isAdmin ? listsService.findAllSystemLists() : listsService.findByOwner(account);

        boolean duplicated = existing.stream().anyMatch(l -> l.getListName().equals(listName));
        if(duplicated){
            return ResponseEntity.status(409).build();
        }

        Lists newList = isAdmin ? listsService.save(listName, Lists.getTypeString(type), null , null) : listsService.save(listName, Lists.getTypeString("USER"), account, null) ;

        return ResponseEntity.status(201).body(listsMapper.toDTO(newList));
    }

    @DeleteMapping("lists/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable Long id, Principal principal){
        if(principal == null){
            return ResponseEntity.status(401).build();
        }
        Lists list = listsService.findById(id);
        if (list == null) return ResponseEntity.notFound().build();

        boolean isAdmin = accountService.findByEmail(principal.getName()).getAccountRole() == Account.Role.ADMIN;
        boolean isOwner = list.getListOwner() != null && list.getListOwner().getAccountEmail().equals(principal.getName());

        if (!isAdmin && !isOwner){ 
            return ResponseEntity.status(403).build();
        }
        listsService.delete(list.getListsId());

        return ResponseEntity.noContent().build();
    }

    // ADMIN
    
    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDto>> getAllAccounts(Principal principal) {
        if(principal == null){
            return ResponseEntity.status(401).build();
        }
        boolean isAdmin = accountService.findByEmail(principal.getName()).getAccountRole() == Account.Role.ADMIN;
        if (!isAdmin){ 
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(accountMapper.toDTOs(accountService.findAll()));
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id, Principal principal) {

        if(principal == null){
            return ResponseEntity.status(401).build();
        }

        boolean isAdmin = accountService.findByEmail(principal.getName()).getAccountRole() == Account.Role.ADMIN;
        if (!isAdmin){ 
            return ResponseEntity.status(403).build();
        }
        if (accountService.findById(id) == null){
            return ResponseEntity.notFound().build();
        }
        accountService.delete(id);

        return ResponseEntity.noContent().build();
    }

}

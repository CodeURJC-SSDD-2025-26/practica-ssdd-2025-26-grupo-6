document.addEventListener('DOMContentLoaded', function() {
    /*const selector = document.getElementById('selectAvatar');*/
    const preview = document.getElementById('previewAvatar');
    const btnGuardar= document.getElementById('saveAvatar');
    const galleryImages = document.querySelectorAll('.img-selectable');
    /*const allTheAvatars = document.querySelectorAll('.user-avatar');*/
    const avatarLogo = document.getElementById('avatarLogo');
    const avatarProfile = document.getElementById('avatarProfile');
    let selectedScr = "images/perfilNoReg.jpg";
    galleryImages.forEach(IMG => {
        IMG.addEventListener('click',function(){
            galleryImages.forEach(i => i.classList.remove('active-avatar'));

            this.classList.add('active-avatar');
            selectedSrc =this.getAttribute('data-value');
            preview.src =selectedSrc;

        })
    })

    btnGuardar.addEventListener('click', function() {
        if(avatarLogo){
            avatarLogo.src = selectedSrc;
        }
        if(avatarProfile){
            avatarProfile.src = selectedSrc;
        }
        });
/*
        document.getElementById('mainProfileAvatar').src = newAvatar;
    });*/
});
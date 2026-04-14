document.addEventListener('DOMContentLoaded', function() {
    
    /* Declare Variables */
    const preview = document.getElementById('previewAvatar');
    const btnSave= document.getElementById('saveAvatar');
    const galleryImages = document.querySelectorAll('.img-selectable');
    
    const avatarLogo = document.getElementById('avatarLogo');
    const avatarProfile = document.getElementById('avatarProfile');
    let selectedSrc = "/images/perfilNoReg.jpg";

    /* Select Avatar */
    galleryImages.forEach(IMG => {
        IMG.addEventListener('click',function(){
            galleryImages.forEach(i => i.classList.remove('active-avatar'));

            this.classList.add('active-avatar');
            selectedSrc =this.getAttribute('data-value');
            preview.src =selectedSrc;

        })
    })

    /* Save the Selection */
    btnSave.addEventListener('click', function() {
        if(avatarLogo){
            avatarLogo.src = selectedSrc;
        }
        if(avatarProfile){
            avatarProfile.src = selectedSrc;
        }
        document.getElementById('avatarInput').value = selectedSrc;
        document.getElementById('avatarForm').submit();
        });
});
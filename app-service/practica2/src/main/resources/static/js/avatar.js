document.addEventListener('DOMContentLoaded', function() {
    
    /* Declare Variables */
    const preview = document.getElementById('previewAvatar');
    const btnSave= document.getElementById('saveAvatar');
    const galleryImages = document.querySelectorAll('.img-selectable');
    
    const avatarLogo = document.getElementById('avatarLogo');
    const avatarProfile = document.getElementById('avatarProfile');
    let selectedId = null;

    /* Select Avatar */
    galleryImages.forEach(IMG => {
        IMG.addEventListener('click',function(){
            galleryImages.forEach(i => i.classList.remove('active-avatar'));

            this.classList.add('active-avatar');
            selectedId =this.getAttribute('data-value');
            preview.src = '/img/'+selectedId;

        })
    })

    /* Save the Selection */
    btnSave.addEventListener('click', function() {
        if(!selectedId) return;
        if(avatarLogo){
            avatarLogo.src = '/img/'+selectedId;
        }
        if(avatarProfile){
            avatarProfile.src = '/img/'+selectedId;
        }
        document.getElementById('avatarInput').value = selectedId;
        document.getElementById('avatarForm').submit();
        });
});
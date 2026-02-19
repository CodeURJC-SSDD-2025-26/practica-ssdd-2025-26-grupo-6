document.addEventListener('DOMContentLoaded', function() {
    const selector = document.getElementById('selectAvatar');
    const preview = document.getElementById('previewAvatar');
    const btnGuardar= document.getElementById('saveAvatar');
    /*const allTheAvatars = document.querySelectorAll('.user-avatar');*/

    selector.addEventListener('change', function() {
        preview.src = this.value
    });

    btnGuardar.addEventListener('click', function() {
        const newAvatar = selector.value;
        avatarLogo.src =newAvatar;
        avatarProfile.src = newAvatar;
        });
/*
        document.getElementById('mainProfileAvatar').src = newAvatar;
    });*/
});
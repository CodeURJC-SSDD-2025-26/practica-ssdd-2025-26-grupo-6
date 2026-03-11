const posterInput = document.getElementById('posterInput');
const posterPreview = document.getElementById('posterPreview');

// if both elements exist, set up the preview functionality
if (posterInput && posterPreview) {

  // 1) Open selector
  posterPreview.addEventListener('click', () => {
    posterInput.click();
  });

  // 2) Preview selected image
  posterInput.addEventListener('change', (e) => {
    const file = e.target.files?.[0];
    if (!file) return;

    const maxMB = 5;
    const allowed = ['image/jpeg', 'image/png', 'image/webp'];

    if (!allowed.includes(file.type)) {
      alert('Formato no admitido. Usa JPG, PNG o WebP.');
      posterInput.value = '';
      return;
    }
    if (file.size > maxMB * 1024 * 1024) {
      alert(`La imagen supera ${maxMB} MB.`);
      posterInput.value = '';
      return;
    }

    // Preview with FileReader
    const reader = new FileReader();
    reader.onload = () => {
      posterPreview.src = reader.result; // Data URL
    };
    reader.readAsDataURL(file);
  });
}

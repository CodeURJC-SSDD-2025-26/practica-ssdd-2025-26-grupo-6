// js/palomix-admin.js

// Referencias a elementos
const posterInput = document.getElementById('posterInput');
const posterPreview = document.getElementById('posterPreview');

// Seguridad: si alguno no existe, no seguimos
if (posterInput && posterPreview) {

  // 1) Abrir selector al pulsar la imagen
  posterPreview.addEventListener('click', () => {
    posterInput.click();
  });

  // 3) Previsualizar cuando el usuario selecciona un archivo
  posterInput.addEventListener('change', (e) => {
    const file = e.target.files?.[0];
    if (!file) return;

    // Validaciones recomendadas
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

    // Previsualizar con FileReader
    const reader = new FileReader();
    reader.onload = () => {
      posterPreview.src = reader.result; // Data URL
    };
    reader.readAsDataURL(file);
  });
}

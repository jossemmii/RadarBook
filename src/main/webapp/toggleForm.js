document.addEventListener('DOMContentLoaded', function() {
    const toggleButton = document.getElementById('toggleButton');
    const searchForm = document.getElementById('searchForm');
    const searchISBNInput = document.getElementById('searchISBNInput');

    toggleButton.addEventListener('click', function() {
        searchForm.classList.toggle('minimized');
        if (searchForm.classList.contains('minimized')) {
            toggleButton.textContent = '+';
        } else {
            toggleButton.textContent = '-';
        }
    });

    // Validación en tiempo real para el campo ISBN
    searchISBNInput.addEventListener('input', function() {
        this.value = this.value.replace(/[^0-9]/g, '');
    });
});
window.addEventListener('DOMContentLoaded', () => {
    const menu = document.getElementById('USER-profileMenu');
    if (menu) menu.style.display = 'none';
});

function toggleUserProfileMenu() {
    const menu = document.getElementById('USER-profileMenu');
    if (!menu) return;
    menu.style.display = menu.style.display === 'block' ? 'none' : 'block';
}

document.addEventListener('click', function(event) {
    const menu = document.getElementById('USER-profileMenu');
    const button = document.querySelector('.USER-profile-button');
    if (menu && !menu.contains(event.target) && !button.contains(event.target)) {
        menu.style.display = 'none';
    }
});

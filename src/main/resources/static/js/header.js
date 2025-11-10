document.addEventListener('DOMContentLoaded', () => {
	const dropdown = document.querySelector('.header-dropdown');
	const icon = document.querySelector('.mypage-icon');
	const menu = document.querySelector('.dropdown-menu');

	if (!dropdown || !icon || !menu) {
		return;
	}

	icon.addEventListener('click', (event) => {
		event.stopPropagation();
		dropdown.classList.toggle('open');
	});

	menu.addEventListener('click', (event) => {
		event.stopPropagation();
	});

	document.addEventListener('click', () => {
		dropdown.classList.remove('open');
	});
});


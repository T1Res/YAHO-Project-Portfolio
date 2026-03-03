// 별점 시스템 초기화 함수 - DOMContentLoaded 또는 동적 생성 후 호출
function initRatingEvents() {
    const rateWraps = document.querySelectorAll('.rating');

    rateWraps.forEach(wrap => {
        const labels = wrap.querySelectorAll('.rating__label');
        const stars = wrap.querySelectorAll('.star-icon');

        labels.forEach((label, idx) => {
            const starIcon = label.querySelector('.star-icon');

            label.addEventListener('mouseenter', () => {
                initStars(wrap);
                fillStars(wrap, idx);
            });

            label.addEventListener('mouseleave', () => {
                initStars(wrap);
                checkStars(wrap);
            });
        });

        wrap.addEventListener('mouseleave', () => {
            initStars(wrap);
            checkStars(wrap);
        });

        // 초기 상태 체크
        checkStars(wrap);
    });
}

// 별점 초기화: 모든 별에서 .filled 제거
function initStars(container) {
    const stars = container.querySelectorAll('.star-icon');
    stars.forEach(star => {
        star.classList.remove('filled');
        star.style.opacity = '1';
    });
}

// 별 채우기: index까지 .filled 클래스 적용
function fillStars(container, index) {
    const stars = container.querySelectorAll('.star-icon');
    for (let i = 0; i <= index && i < stars.length; i++) {
        stars[i].classList.add('filled');
        stars[i].style.opacity = '0.5';
        console.log(`★ 채움 index: ${i}`);
    }
}

// 체크된 별 반영
function checkStars(container) {
    initStars(container);

    const checkedRadio = container.querySelector('input[type="radio"]:checked');
    if (checkedRadio) {
        const labels = Array.from(container.querySelectorAll('.rating__label'));
        const index = labels.findIndex(label => label.contains(checkedRadio));
        if (index !== -1) {
            for (let i = 0; i <= index && i < labels.length; i++) {
                const icon = labels[i].querySelector('.star-icon');
                icon.classList.add('filled');
            }
        }
    }
}

// 초기 로딩 시 실행
document.addEventListener('DOMContentLoaded', () => {
    initRatingEvents();
});

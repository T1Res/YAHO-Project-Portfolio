function updateRate() {
    const scoreInput = document.querySelector('input[name="SCORE_SCORE"]:checked');
    const score = scoreInput ? scoreInput.value : null;

    const contentElement = document.querySelector('textarea[name="SCORE_CONTENT"]');
    const content = contentElement ? contentElement.value.trim() : "";

    const animeId = document.getElementById('rated_anime_id')?.value;
    const userId = document.getElementById('rated_user_id')?.value;
	
		console.log(animeId);
		console.log(userId);
		console.log(content);
		console.log(score);

    if (!score || !content || !animeId || !userId) {
        alert('모든 정보를 입력해주세요.');
        return;
    }

    fetch('/Schedule/ajaxUpdate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            SCORE_SCORE: score,
            SCORE_CONTENT: content,
            ANIME_ID: animeId,
            USER_ID: userId
        })
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            alert('수정 완료!');
            refreshAll(animeId, userId);  // 등록과 동일하게 갱신
			
			
        } else {
            alert('수정 실패');
        }
    })
    .catch(err => {
        console.error('수정 중 오류 발생', err);
        alert('서버 오류');
    });
}

function generateStars(selectedScore) {
    let html = '';
    for (let i = 0.5; i <= 5; i += 0.5) {
        const checked = (i === parseFloat(selectedScore)) ? 'checked' : '';
        const id = 'star' + i.toString().replace('.', '');
        const labelClass = (i % 1 === 0) ? 'rating__label--full' : 'rating__label--half';

        html += `
        <label class="rating__label ${labelClass}" for="${id}">
            <input type="radio" id="${id}" class="rating__input" name="SCORE_SCORE" value="${i}" ${checked} onclick="handleStarClick(this)"/>
            <span class="star-icon"></span>
        </label>
        `;
    }
    return html;
}

// 별점 클릭 시 동작할 함수
function handleStarClick(el) {
    console.log("별점 선택됨:", el.value);
    // 필요한 경우 여기에 별 아이콘 색상 변경 등 효과 추가
}

window.updateRate = updateRate;

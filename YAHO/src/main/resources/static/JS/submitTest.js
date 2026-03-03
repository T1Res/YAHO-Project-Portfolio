function submitRate() {
    const scoreInput = document.querySelector('input[name="SCORE_SCORE"]:checked');
    const score = scoreInput ? scoreInput.value : null;

    const contentElement = document.querySelector('#registerFormArea textarea[name="SCORE_CONTENT"]');
    const content = contentElement ? contentElement.value.trim() : "";

    const animeId = document.getElementById('register_anime_id')?.value;
    const userId = document.getElementById('register_user_id')?.value;
	console.log(animeId);
	console.log(userId);
	console.log(content);
	console.log(score);
	
    if (!score || !content || !animeId || !userId) {
        alert('별점, 댓글, 사용자 ID, 애니 ID를 모두 입력해주세요!');
        return;
    }

    fetch('/Schedule/ajaxInsert', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
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
            alert('등록 완료!');
            refreshAll(animeId, userId);
			// ✅ 등록 폼 영역 날리기
				const registerForm = document.getElementById("registerFormArea");
				if (registerForm) {
					console.log("등록폼 날림");
					registerForm.remove();  // ✅ 완전히 삭제
				}
			
            const rateFormArea = document.getElementById("rateFormArea");
            if (rateFormArea && !rateFormArea.querySelector("form")) {
                rateFormArea.innerHTML = "";
                rateFormArea.appendChild(buildEditForm(userId, animeId, score, content));
				
            }
        } else {
            alert('등록 실패!');
        }
    })
    .catch(err => {
        console.error(err);
        alert('오류가 발생했습니다.');
    });
}

function refreshAll(animeId, userId) {
    fetch('/Schedule/refreshRate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ ANIME_ID: animeId, USER_ID: userId })
    })
    .then(res => res.json())
    .then(data => {
        const listDiv = document.querySelector(".wrap:last-child");
        listDiv.innerHTML = "<h1>게시판 목록</h1>";
        data.list.forEach(dto => {
            const starsHtml = "⭐".repeat(dto.SCORE_SCORE);
            listDiv.innerHTML += `
                <div style="margin-bottom:20px;">
                    <div style="color:white;"><strong>${dto.USER_ID}</strong> :</div>
                    <div style="color:gold; font-size:20px;">${starsHtml}</div>
                    <div style="color:white; margin-top:4px;"><span>${dto.SCORE_CONTENT}</span></div>
                    <hr style="margin-top:20px;" />
                </div>
            `;
        });

        document.querySelector(".grade-section span").textContent = data.grade;
        document.querySelector(".grade-badge").textContent = data.mark;

        drawGenderChart(data.maleRatio, data.femaleRatio);
        updateBarChart(data.scoreList, data.countList);
    });
}

function updateBarChart(scoreList, countList) {
    const chartCanvas = document.getElementById('myChart');
    if (!chartCanvas) return;

    const ctx2 = chartCanvas.getContext('2d');
    if (window.myChart?.destroy) window.myChart.destroy();

    chartCanvas.width = 300;
    chartCanvas.height = 200;

    window.myChart = new Chart(ctx2, {
        type: 'bar',
        data: {
            labels: scoreList,
            datasets: [{
                label: '평점 분포',
                data: countList,
                backgroundColor: [
                    'rgba(255, 99, 132, 0.6)',
                    'rgba(54, 162, 235, 0.6)',
                    'rgba(255, 206, 86, 0.6)',
                    'rgba(75, 192, 192, 0.6)',
                    'rgba(153, 102, 255, 0.6)'
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)'
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: false,
            maintainAspectRatio: false,
            scales: {
                y: { beginAtZero: true }
            }
        }
    });
}

function buildEditForm(userId, animeId, score, content) {
    const form = document.createElement("form");
    form.onsubmit = (e) => {
        e.preventDefault();
        updateRate();
    };

    const html = `
        <input type="hidden" name="USER_ID" value="${userId}">
        <input type="hidden" name="ANIME_ID" value="${animeId}">
        <div class="rating">${generateStars(score)}</div>
		<br>
        <textarea name="SCORE_CONTENT" rows="4" cols="50">${content}</textarea>
		<br>
        <button type="submit">수정</button>
		<button type="button" onclick="deleteRate()">삭제</button>
    `;

    form.innerHTML = html;

    // ⭐ 여기서 직접 .filled 클래스 채워주기
    setTimeout(() => {
        const stars = form.querySelectorAll('input[name="SCORE_SCORE"]');
        stars.forEach((input, idx) => {
            if (input.checked) {
                for (let i = 0; i <= idx; i++) {
                    const star = stars[i].closest('label').querySelector('.star-icon');
                    star.classList.add('filled');
                }
            }
        });
		
		initRatingEvents();
    }, 0); // DOM 붙은 후 실행

    return form;
}

function generateStars(score) {
    let html = '';
    for (let i = 0.5; i <= 5; i += 0.5) {
        const id = 'star' + i.toString().replace('.', '');
        const checked = i === parseFloat(score) ? 'checked' : '';
        const labelClass = i % 1 === 0 ? 'rating__label--full' : 'rating__label--half';

        html += `
            <label class="rating__label ${labelClass}" for="${id}">
                <input type="radio" id="${id}" class="rating__input" name="SCORE_SCORE" value="${i}" ${checked}>
                <span class="star-icon"></span>
            </label>
        `;
    }
    return html;
}
function deleteRate() {
    const animeId = document.querySelector('input[name="ANIME_ID"]')?.value;
    const userId = document.querySelector('input[name="USER_ID"]')?.value;

    if (!animeId || !userId) {
        alert("애니 ID나 사용자 ID가 없습니다.");
        return;
    }

    if (!confirm("정말 삭제하시겠습니까?")) return;

    fetch('/Schedule/ajaxDelete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            ANIME_ID: animeId,
            USER_ID: userId
        })
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            alert('삭제 완료!');

            // 수정 폼 초기화
            const rateFormArea = document.getElementById("rateFormArea");
            if (rateFormArea) rateFormArea.innerHTML = '';

            // 등록 폼 복구
			let registerForm = document.getElementById("registerFormArea");
			if (!registerForm) {
			    registerForm = document.createElement("div");
			    registerForm.id = "registerFormArea";

			    // 원하는 위치에 추가 (예: rateFormArea 바로 앞)
			    const rateFormArea = document.getElementById("rateFormArea");
			    rateFormArea?.parentNode.insertBefore(registerForm, rateFormArea);
			}

			registerForm.innerHTML = `
			    <input type="hidden" id="userId" name="USER_ID" value="${userId}" />
			    <input type="hidden" id="animeId" name="ANIME_ID" value="${animeId}" />
			    <div class="rating">${generateStars(0)}</div>
			    <div style="margin-top: 20px;">
			        <textarea name="SCORE_CONTENT" rows="4" cols="50" placeholder="댓글을 입력해주세요"></textarea>
			    </div>
			    <button type="button" onclick="submitRate()">등록</button>
			`;

			registerForm.style.display = 'block';
			
            

            refreshAll(animeId, userId);
			
			initRatingEvents();
        } else {
            alert('삭제 실패');
        }
    })
    .catch(err => {
        console.error('삭제 중 오류 발생', err);
        alert('서버 오류');
    });
}
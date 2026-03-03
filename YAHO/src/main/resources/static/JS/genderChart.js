function drawGenderChart(maleRatio, femaleRatio) {
  const ctx = document.getElementById('genderChart').getContext('2d');

  // 기존 차트가 있으면 제거
  if (window.genderChartInstance) {
    window.genderChartInstance.destroy();
  }

  // 새 차트 생성
  window.genderChartInstance = new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: ['남성', '여성'],
      datasets: [{
        label: '성별 투표 비율',
        data: [maleRatio, femaleRatio],
        backgroundColor: [
          'rgba(0, 153, 255, 0.7)',
          'rgba(255, 102, 204, 0.7)'
        ],
        borderColor: [
          'rgba(0, 153, 255, 1)',
          'rgba(255, 102, 204, 1)'
        ],
        borderWidth: 1
      }]
    },
    options: {
      cutout: '50%',
      responsive: true,
      plugins: {
        legend: {
          position: 'bottom',
          labels: {
            color: '#fff',
            font: {
              size: 14
            }
          }
        },
        tooltip: {
          callbacks: {
            label: function (context) {
              return `${context.label}: ${context.raw}%`;
            }
          }
        }
      }
    }
  });
}
window.addEventListener("DOMContentLoaded", () => {
  if (typeof drawGenderChart === 'function') {
    drawGenderChart(maleRatio, femaleRatio);
  }
});
// ✅ 전역으로 함수 노출
window.drawGenderChart = drawGenderChart;
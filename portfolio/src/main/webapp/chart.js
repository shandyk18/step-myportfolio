function onLoad() {
    console.log("HELLO");
    voteLogin();
}

function voteLogin() {
  fetch('/login').then(response => response.json()).then((status) => {
    if (status.status) {
        document.getElementById('color-vote-form').style.display = 'block';
        document.getElementById('color-login-form').style.display = 'none';
    } else {
        document.getElementById('color-vote-form').style.display = 'none';
        document.getElementById('color-login-form').style.display = 'block';
        document.getElementById('color-login-link').href = status.link;
    }
  });
}

google.charts.load('current', {
    'packages': ['corechart']
});
google.charts.setOnLoadCallback(drawChart);

/** Creates a chart and adds it to the page. */
function drawChart() {
  fetch('/survey').then(response => response.json())
  .then((colorVotes) => {
    const data = new google.visualization.DataTable();
    data.addColumn('string', 'Color');
    data.addColumn('number', 'Votes');
    data.addColumn({ role: 'style' }, 'Color')
    Object.keys(colorVotes).forEach((color) => {
      data.addRow([color, colorVotes[color], color.toLowerCase()]);
    });

    const options = {
      'title': 'Favorite Colors',
      'width': 600,
      'height': 500,
      'legend': {position: 'none'}
    };

    const chart = new google.visualization.ColumnChart(
        document.getElementById('color-container'));
    chart.draw(data, options);
  });
}
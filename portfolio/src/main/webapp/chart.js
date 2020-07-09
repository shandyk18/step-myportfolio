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
    Object.keys(colorVotes).forEach((color) => {
      data.addRow([color, colorVotes[color]]);
    });

    const options = {
      'title': 'Favorite Colors',
      'width':600,
      'height':500
    };

    const chart = new google.visualization.ColumnChart(
        document.getElementById('chart-container'));
    chart.draw(data, options);
  });
}
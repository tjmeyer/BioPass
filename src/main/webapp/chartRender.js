/* global capturedList */

window.onload = function (){
    var chart = new CanvasJS.Chart("chartContainer",
    {
        title:{
          text: "Biometric Profile"        
        },
        theme: "theme5",
        animationEnabled: true,
        axisX: {
            title: "Password typing time (ms)",
            interval : 100
        },
        axisY: {
            title: "Duration in Milliseconds"  
        },
        legend: {
          verticalAlign: "bottom",
          horizontalAlign: "center"
        },
        toolTip: {
          shared: true
        },
        data: [
        {        
            indexLabelFontColor: "teal",
            type: "area",  
            showInLegend: true, 
            name: "Downtime",
            dataPoints: [      
            ]
        },
        {        
            indexLabelFontColor: "red",
            type: "area",  
            showInLegend: true, 
            name: "Uptime",
            dataPoints: [      
            ]
        }

        ]
    });

    chart.render();

    $("#updateChart").click(function () {
//	var length = chart.options.data[0].dataPoints.length;
        var startTime = capturedList[0].start;
	for (var i = 0; i < capturedList.length; i++)
        {
            var label = capturedList[i].toString();
            chart.options.data[0].dataPoints.push({x: (capturedList[i].start - startTime), y: capturedList[i].time, indexLabel: label});
            var releaseTime = capturedList[i].start + capturedList[i].time;
            var uptime;
            if (capturedList[i+1])
            {
                uptime = capturedList[i+1].start - releaseTime;
            }
            else
            {
                uptime = 0;
            }
            chart.options.data[1].dataPoints.push({x: releaseTime - startTime, y: uptime, indexLabel: label});
        }
        $("#reset").prop("disabled",true);
        $("#updateChart").prop("disabled",true);
	chart.render();
    });
};



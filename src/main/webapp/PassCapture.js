var capturedList = new Array(); // This is orded by when keys are PRESSED
var processList = new Array(); // This is an interim list for ensuring that key duplicates are not confused.
var currentID = 0;

function capture(ID, keyPressed){
    this.id     = ID;
    this.start  = new Date().getTime();
    this.key    = keyPressed;
    this.time   = null;
    this.endCapture = function() {
        this.time = new Date().getTime() - this.start;
        return;
    };
    
    this.toString = function()
    {
        var label = String.fromCharCode(this.key);
        if(!label.match(/^[0-9a-zA-Z]+$/))
        {
            if (this.key === 16)
            {
                label = "SHIFT";
            }
            else if (this.key === 17)
            {
                label = "CTRL";
            }
            else if (this.key === 13)
            {
                label = "ENTER";
            }
            else
            {
                label = "UNKNOWN";
            }
        }
        return label;
    }
};

function findById(source, id)
{
    for(var i = 0; i < source.length; i++)
    {
        if (source[i].id === id)
        {
            return source[i];
        }
    }
    throw "Coun't find object with id: " + id;
}

function findByKey(source, key)
{
    for(var i = 0; i < source.length; i++)
    {
        if (source[i].key === key)
        {
            return i;
        }
    }
    throw "Coun't find object with key: " + key;    
}

function capturedListToString()
{
    var result = "";
    for (var i = 0; i < capturedList.length; i++)
    {
        result += capturedList[i].key + ":{" + capturedList[i].start + ":" + capturedList[i].time + "} ";
    
    }
    return result;
}

function startCapture(event)
{
    var capturePoint    = new capture(currentID, event.which);
    processList.push(capturePoint);
    currentID++;
}

function endCapture(event)
{
    var index = findByKey(processList, event.which);
    processList[index].endCapture();
    capturedList.push(processList[index]);
    processList.splice(index, 1);
    capturedList.sort(compare);
    display();
}

function compare(a, b)
{
    if(a.start < b.start)
        return -1;
    if(a.start > b.start)
        return 1;
    return 0;
}

function display()
{
    document.getElementById("messages").innerHTML = capturedListToString();
}

function reset()
{
    capturedList = new Array(); // This is orded by when keys are PRESSED
    processList = new Array(); // This is an interim list for ensuring that key duplicates are not confused.
    currentID = 0;
    document.getElementById('passwordInput').value = "";
    document.getElementById('messages').innerHTML = "";
    document.getElementById('passwordInput').focus();
}

function passCapture()
{
    var usernamed = $('#username').val();
 
    $.post('CheckUsername', {username: usernamed, arg: capturedListToString()}, function(response)
    {
        $('#messages').text(response);
    }); 
}
//при нажатии на график или на кнопку отправки можно вызывать функцию
// парса таблицы и рисовать стили строчкам
let arr_x = [-3, -2, -1, 0, 1, 2, 3, 4, 5];

let elem_y = document.getElementById("coords_form:input_y");
elem_y.setAttribute("value", "");


let canvas = document.getElementById('canvas');
canvas.height = 500;
canvas.width = 500;

function drawCanvas() {

    let con = canvas.getContext('2d');
    con.beginPath();
    con.clearRect(0, 0, canvas.width, canvas.height);
    //con.fillStyle = "rgb(255, 255, 255)";
    //con.fillRect(0, 0, canvas.width, canvas.height);
    con.fillStyle = '#3399ff';
    con.lineWidth = 2.0;

    con.beginPath();
    con.moveTo(250, 15);
    con.lineTo(250, 485);
    con.stroke();

    con.beginPath();
    con.moveTo(15, 250);
    con.lineTo(485, 250);
    con.stroke();

    con.beginPath();
    con.arc(250, 250, 200, 0, -3 * Math.PI / 2);
    con.lineTo(250, 250);
    con.lineTo(500 - 15, 250);
    con.fill();
    con.stroke();

    con.beginPath();
    con.moveTo(50, 250);
    con.lineTo(250, 250);
    con.lineTo(250, 250 + 100);
    con.fill();
    con.stroke();


    con.beginPath();
    con.fillRect(251, 49, 199, 200); // (x,y) - центр, потом (width,height)
    con.stroke();

    //север
    con.moveTo(240, 50);
    con.lineTo(260, 50);
    con.fillStyle = 'black';
    con.font = "30px Verdana";
    con.fillText("R", 260, 50)

    //восток
    con.moveTo(450, 240);
    con.lineTo(450, 260);
    con.fillText("R", 455, 250)

    //юг
    con.moveTo(240, 450);
    con.lineTo(260, 450);
    con.fillText("-R", 270, 460)

    //запад
    con.moveTo(50, 240);
    con.lineTo(50, 260);
    con.fillText("-R", 15, 250)

    con.stroke();

}

drawCanvas();

document.getElementById("canvas").addEventListener("click", function (e) {
    let r = validateR(true);
    if (r === false) {
        return;
    }

    let x = (e.offsetX - 250) / 200 * r;
    let y = -e.offsetY + 250;

    let Realy = y / 200 * r;
    let Realx;
    let minDiff = Infinity;
    for (let i = 0; i < arr_x.length; i++) {
        if (Math.abs(arr_x[i] - x) < minDiff) {
            minDiff = Math.abs(arr_x[i] - x);
            Realx = arr_x[i];
        }
    }

    elem_y.setAttribute("value", Realy);

    let btns = $("[name='coords_form:button_x']");

    for (let btn of btns) {
        if (btn.value == Realx) {
            btn.setAttribute("checked", true);
        }
    }

    addPoint([{name: 'x', value: Realx}, {name: 'y', value: Realy}, {name: 'r', value: r}]).then((responseData) => {
        let verdict = responseData.jqXHR.pfArgs.verdict;
        console.log(verdict);
        let con = canvas.getContext('2d');
        if (verdict === "INCLUDED") {
            con.fillStyle = '#008000';
        } else {
            con.fillStyle = '#900';
        }

        con.beginPath();
        con.arc(Realx / r * 200 + 250, e.offsetY, 4, 0, 2 * Math.PI)
        con.fill();
        con.stroke();
        //con.beginPath();
        addColor();
    });
});


function validateR(isAlert) {
    let elem_r = document.getElementById('coords_form:r').value;

    let mas = ['1', '1.5', '2', '2.5', '3', '3.5', '4'];
    let flag = false;
    for (let i of mas) {
        if (elem_r === i) {
            flag = true;
        }
    }

    if (flag) {
        return elem_r;
    }
    if (isAlert) alert("please, enter correct value in the field for R");
    return false;
}


function go() {
    let elem_r = document.getElementById('coords_form:r').value;
    let points = [];

    let tableElements = document.querySelectorAll("table#results > tbody > tr");

    Array.from(tableElements, e => {
        let childNodes = e.getElementsByTagName("td");
        points.push(new Object({
            x: childNodes[0].textContent,
            y: childNodes[1].textContent,
            r: childNodes[2].textContent,
            verdict: childNodes[3].textContent,
        }));
    });
    canvas.getContext('2d').beginPath();
    canvas.getContext('2d').clearRect(0, 0, canvas.width, canvas.height);
    //canvas.getContext('2d').fillStyle = "rgb(255, 255, 255)";
    //canvas.getContext('2d').fillRect(0, 0, canvas.width, canvas.height);
    //canvas.getContext('2d').beginPath();
    drawCanvas();
    drawTablePoints(points, elem_r);
}


function drawTablePoints(points, elem_r) {

    if (points.length == 1 && points[0].x == "") return;
    let con = canvas.getContext('2d');
    for (let i = 0; i < points.length; i++) {
        //у меня значение не 4 а 4.0
        if (elem_r == Number(points[i].r)) {
            if (points[i].verdict === 'INCLUDED') {
                con.fillStyle = '#008000';
            } else {
                con.fillStyle = '#900';
            }
            con.beginPath();
            con.arc(points[i].x / points[i].r * 200 + 250,
                -(points[i].y / points[i].r) * 200 + 250,
                4, 0, 2 * Math.PI)

            con.fill();
            con.stroke();
            //con.beginPath();
        }
    }
}

function addColor() {
    let tableElements = document.querySelectorAll("table#results > tbody > tr");
    Array.from(tableElements, e => {
        let childNodes = e.getElementsByTagName("td");
        if (childNodes[3].innerHTML === "INCLUDED") {
            e.classList.add("green");
        }
        if (childNodes[3].innerHTML === "NOT_INCLUDED"){
            e.classList.add("red");
        }
    });
}

function drawPoint() {
    let r = validateR(false);
    if (r === false) return;
    r = Number(r);
    let selected_btn_x = null;
    let btns = $("[name='coords_form:button_x']");
    for (let btn of btns) {
        if (btn.checked) {
            selected_btn_x = btn;
        }
    }

    if (selected_btn_x === null) return;
    let x = Number(selected_btn_x.value);
    let y = document.getElementById("coords_form:input_y").value;
    if (!(!isNaN(parseFloat(y)) && isFinite(y))) {
        return;
    }

    if(!(Number(y)>=-5 && Number(y)<=5)){
        return;
    }
    y = Number(y);
    console.log("success");

    getVerdict().then((responseData) => {
        let verdict = responseData.jqXHR.pfArgs.verdict;
        console.log(verdict);
        let con = canvas.getContext('2d');
        if (verdict === "INCLUDED") {
            con.fillStyle = '#008000';
        } else {
            con.fillStyle = '#900';
        }
        con.beginPath();
        con.arc(x / r * 200 + 250,
            -(y / r) * 200 + 250,
            4, 0, 2 * Math.PI)
        con.fill();
        con.stroke();
        addColor();
    });
}

window.onload = addColor();

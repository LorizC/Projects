import turtle # turtle.bgcolor() turtle.speed()
from turtle import * #bgcolor() speed()
from random import randint

window=turtle.Screen()
window.title('PyTurt Racing')

turtle.bgcolor('green')
turtle.speed(0)
turtle.penup()
turtle.setpos(-140,200)
turtle.write("TURTLE RACE",font=('Arial',30))

turtle.setpos(-400,-180)
turtle.color('brown')
turtle.begin_fill()
turtle.pendown()
for i in range(2):
    turtle.forward(800)
    turtle.right(90)
    turtle.forward(300)
    turtle.right(90)
turtle.end_fill()

stamp_size=20
square_size=15
finish_line=200
#Race set up/Finish Line
turtle.color('black')
turtle.shape('square')
turtle.penup()

for i in range(10):
    turtle.setpos(finish_line,(150-(i*square_size*2)))
    turtle.stamp()

for j in range(10):
    turtle.setpos(finish_line+square_size,((150-square_size)-(j*square_size*2)))
    turtle.stamp()

turtle1 = Turtle()
turtle1.speed(0)
turtle1.color('red')
turtle1.shape('turtle')
turtle1.penup()
turtle1.goto(-250, 100)
turtle1.pendown()

turtle2 = Turtle()
turtle2.speed(0)
turtle2.color('blue')
turtle2.shape('turtle')
turtle2.penup()
turtle2.goto(-250, 35)#50 65
turtle2.pendown()

turtle3 = Turtle()
turtle3.speed(0)
turtle3.color('pink')
turtle3.shape('turtle')
turtle3.penup()
turtle3.goto(-250, -35)#0 65
turtle3.pendown()

turtle4 = Turtle()
turtle4.speed(0)
turtle4.color('purple')
turtle4.shape('turtle')
turtle4.penup()
turtle4.goto(-250, -100)#50
turtle4.pendown()

t1=0
t2=0
t3=0
t4=0

for i in range(180):
    k = randint(1,5)
    l = randint(1, 5)
    m = randint(1, 5)
    n = randint(1, 5)
    turtle1.forward(k)
    turtle2.forward(l)
    turtle3.forward(m)
    turtle4.forward(n)

    t1+=k
    t2+=l
    t3+=m
    t4+=n

    if t1>=480:
        turtle1.penup()
        turtle1.goto(0, -120)
        turtle.goto(0,-150)
        turtle.write("Winner is Turtle 1",font=("Arial", 18))
        break
        
    elif t2>=480:
        turtle2.penup()
        turtle2.goto(0, -120)
        turtle.goto(0,-150)
        turtle.write("Winner is Turtle 2",font=("Arial", 18))
        break
        
    elif t3>=480:
        turtle3.penup()
        turtle3.goto(0, -120)
        turtle.goto(0,-150)
        turtle.write("Winner is Turtle 3",font=("Arial", 18))
        break
        
    elif t4>=480:
        turtle4.penup()
        turtle4.goto(0, -120)
        turtle.goto(0,-150)
        turtle.write("Winner is Turtle 4",font=("Arial", 18))
        break

turtle.hideturtle()
turtle.done()

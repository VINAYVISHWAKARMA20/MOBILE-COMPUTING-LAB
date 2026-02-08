import turtle

def draw_shape(t, type, size, color, name):
    t.pendown()
    t.color("black", color)
    t.begin_fill()
    
    if type == "circle":
        t.circle(size/2)
    elif type == "star":
        for _ in range(5):
            t.forward(size)
            t.right(144)
    elif type == "rect":
        for _ in range(2):
            t.forward(size * 1.5)
            t.left(90)
            t.forward(size)
            t.left(90)
    elif type == "square":
        for _ in range(4):
            t.forward(size)
            t.left(90)
    elif type == "triangle":
        for _ in range(3):
            t.forward(size)
            t.left(120)
            
    t.end_fill()
    t.penup()
    t.setheading(270)
    t.forward(30)
    t.write(name, align="center", font=("Verdana", 10, "bold"))
    t.backward(30)
    t.setheading(0)

def main():
    screen = turtle.Screen()
    screen.setup(width=900, height=400)
    t = turtle.Turtle()
    t.speed(3)
    
    shape_list = [
        ("circle", "#FF5733", "Circle"),
        ("star", "#FFD700", "Star"),
        ("rect", "#33FF57", "Rectangle"),
        ("square", "#3357FF", "Square"),
        ("triangle", "#F333FF", "Triangle")
    ]

    current_x = -350 
    
    for type, color, name in shape_list:
        t.penup()
        t.goto(current_x, 0)
        draw_shape(t, type, 70, color, name)
        current_x += 160 
        
    t.hideturtle()
    turtle.done()

if __name__ == "__main__":
    main()

package com.cliff.stefan;
import com.jogamp.opengl.GL2;

/*
    Stefan Cliff 2019/230449 June 22
    The colours I will be using for the shapes are:
    Square              - red                   - vec3(0.667f, 0.239f, 0.176f)
    Parallelogram       - dark red/brown        - vec3(0.333f, 0.141f, 0.114f)
    Small Triangles     - orange                - vec3(0.745f, 0.412f, 0.282f)
    Medium Triangle     - gray-ish              - vec3(0.651f, 0.580f, 0.573f)
    Big Triangles       - a kind of pale purple - vec3(0.447f, 0.357f, 0.38f)
*/
public class Animals_Tangram {

    // This function just draws an outline of the shape that is currently being selected and moves it
    // ever so slightly ahead/in-front of the current shape
    private static void redOutline(GL2 gl) {
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glTranslated(0.0, 0.0, 0.002);
    }
    public static void cat(GL2 gl, int traverse) {

        /*

            Order   Shape                   Part
             1      Square                  (cats head, 45-degree angle )
             2      Parallelogram           (tail,      45-degree angle )
             3      ST1 (Small Triangle)    (left ear,  45-degree angle )
             4      ST2                     (right ear, 225-degree angle)
             5      Triangle                (chest,     normal)
             6      BT1                     (back,      45-degree angle )
             7      BT2                     (feet,      45-degree angle )

        */
        // square
        gl.glPushMatrix();
        if (traverse == 0) {
            redOutline(gl);
        } else {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glTranslated(-.95f, 0.85f, 0.0f);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        gl.glRotatef(45, 0.0f, 0.0f, 1.0f);
        Shapes_Tangram.drawSquare(gl, 0.2);
        gl.glPopMatrix();

        // parallelogram
        gl.glPushMatrix();
        if (traverse == 1) {
            redOutline(gl);
        } else {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glTranslated(0.972, -0.988, 0.0);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        gl.glRotatef(45, 0.0f, 0.0f, 1.0f);
        Shapes_Tangram.drawParallelogram(gl, 0.2);
        gl.glPopMatrix();

        // small triangle one
        gl.glPushMatrix();
        if (traverse == 2) {
            redOutline(gl);
        } else {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glTranslated(-1.162f, 1.28f, 0.0);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        gl.glRotatef(45, 0.0f, 0.0f, 1.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // small triangle two
        gl.glPushMatrix();
        if (traverse == 3) {
            redOutline(gl);
        } else {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glTranslated(-0.737f, 1.28f, 0.0); // X , Y , Z
        gl.glScalef(3.0f, 3.0f, 3.0f);
        gl.glRotatef(225, 0.0f, 0.0f, 1.0f); // angle, X, Y, Z - axis
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // medium triangle
        gl.glPushMatrix();
        if (traverse == 4) {
            redOutline(gl);
        } else {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glTranslated(-0.737f, 0.204f, 0.0f);
        gl.glRotatef(0, 0.0f, 0.0f, 1.0f);
        gl.glScalef(4.26f, 4.26f, 4.26f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // big triangle one
        gl.glPushMatrix();
        if (traverse == 5) {
            redOutline(gl);
        } else {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glTranslated(-0.091f, 0.0, 0.0);
        gl.glRotatef(45, 0.0f, 0.0f, 1.0f);
        gl.glScalef(6.0f, 6.0f, 6.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // big triangle two
        gl.glPushMatrix();
        if (traverse == 6) {
            redOutline(gl);
        } else {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glTranslated(0.035f, -0.9f, 0.0);
        gl.glRotatef(0, 0.0f, 0.0f, 1.0f);
        gl.glScalef(6.0f, 6.0f, 6.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();
    }
    public static void catSolution(GL2 gl){

        // square
        gl.glPushMatrix();
        gl.glTranslated(-.95f, 0.85f, 0.0f);
        gl.glColor3f(0.667f,0.239f,0.176f);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        gl.glRotatef(45, 0.0f, 0.0f, 1.0f);
        Shapes_Tangram.drawSquare(gl, 0.2);
        gl.glPopMatrix();

        // parallelogram
        gl.glPushMatrix();
        gl.glColor3f(0.333f,0.141f,0.114f);
        gl.glTranslated(0.972, -0.988, 0.0);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        gl.glRotatef(45, 0.0f, 0.0f, 1.0f);
        Shapes_Tangram.drawParallelogram(gl, 0.2);
        gl.glPopMatrix();

        // small triangle one
        gl.glPushMatrix();
        gl.glColor3f(0.745f,0.412f,0.282f);
        gl.glTranslated(-1.162f, 1.28f, 0.0);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        gl.glRotatef(45, 0.0f, 0.0f, 1.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // small triangle two
        gl.glPushMatrix();
        gl.glColor3f(0.745f,0.412f,0.282f);
        gl.glTranslated(-0.737f, 1.28f, 0.0);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        gl.glRotatef(225, 0.0f, 0.0f, 1.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // medium triangle
        gl.glPushMatrix();
        gl.glColor3f(0.651f,0.58f,0.573f);
        gl.glTranslated(-0.737f, 0.204f, 0.0f);
        gl.glRotatef(0, 0.0f, 0.0f, 1.0f);
        gl.glScalef(4.26f, 4.26f, 4.26f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // big triangle one
        gl.glPushMatrix();
        gl.glColor3f(0.447f,0.357f,0.38f);
        gl.glTranslated(-0.091f, 0.0, 0.0);
        gl.glRotatef(45, 0.0f, 0.0f, 1.0f);
        gl.glScalef(6.0f, 6.0f, 6.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // big triangle two
        gl.glPushMatrix();
        gl.glColor3f(0.447f,0.357f,0.38f);
        gl.glTranslated(0.035f, -0.9f, 0.0);
        gl.glRotatef(0, 0.0f, 0.0f, 1.0f);
        gl.glScalef(6.0f, 6.0f, 6.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

    }
    public static void bird(GL2 gl, int traverse){

        // square
        gl.glPushMatrix();
        if (traverse == 0) {
            redOutline(gl);
        } else {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glTranslated(0, 0, 0.0);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        gl.glRotatef(0, 0.0f, 0.0f, 1.0f);
        Shapes_Tangram.drawSquare(gl, 0.2);
        gl.glPopMatrix();

        // parallelogram
        gl.glPushMatrix();
        if (traverse == 1) {
            redOutline(gl);
        } else {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glTranslated(0.95f, 0.088f, 0.0);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        gl.glRotatef(135, 0.0f, 0.0f, 1.0f);
        gl.glRotatef(180, 1.0f, 0.0f, 0.0f);
        Shapes_Tangram.drawParallelogram(gl, 0.2);
        gl.glPopMatrix();

        // small triangle one
        gl.glPushMatrix();
        if (traverse == 2) {
            redOutline(gl);
        } else {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glTranslated(-0.449f, 0.15f, 0.0);
        gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // small triangle two
        gl.glPushMatrix();
        if (traverse == 3) {
            redOutline(gl);
        } else {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glTranslated(-1.313f, 0.507f, 0.0);
        gl.glRotatef(135, 0.0f, 0.0f, 1.0f);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // medium triangle
        gl.glPushMatrix();
        if (traverse == 4) {
            redOutline(gl);
        } else {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glTranslated(0.605, -0.3, 0.0);
        gl.glRotatef(45, 0.0f, 0.0f, 1.0f);
        gl.glScalef(4.26f, 4.26f, 4.26f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // big triangle one
        gl.glPushMatrix();
        if (traverse == 5) {
            redOutline(gl);
        } else {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glTranslated(-0.05, 0.73f, 0.0);
        gl.glRotatef(135, 0.0f, 0.0f, 1.0f);
        gl.glScalef(6.0f, 6.0f, 6.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // big triangle two
        gl.glPushMatrix();
        if (traverse == 6) {
            redOutline(gl);
        } else {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glTranslated(0.8, 0.73, 0.0);
        gl.glRotatef(315, 0.0f, 0.0f, 1.0f);
        gl.glScalef(6.0f, 6.0f, 6.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();


    }

    public static void birdSolution(GL2 gl){
        // square
        gl.glPushMatrix();
        gl.glColor3f(0.667f,0.239f,0.176f);
        gl.glTranslated(0f, 0f, 0.0f);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        gl.glRotatef(0, 0.0f, 0.0f, 1.0f);
        Shapes_Tangram.drawSquare(gl, 0.2);
        gl.glPopMatrix();

        // parallelogram
        gl.glPushMatrix();
        gl.glColor3f(0.333f,0.141f,0.114f);
        gl.glTranslated(0.95f, 0.088f, 0.0f);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        gl.glRotatef(135, 0.0f, 0.0f, 1.0f);
        gl.glRotatef(180, 1.0f, 0.0f, 0.0f);
        Shapes_Tangram.drawParallelogram(gl, 0.2);
        gl.glPopMatrix();

        // small triangle one
        gl.glPushMatrix();
        gl.glColor3f(0.745f,0.412f,0.282f);
        gl.glTranslated(-0.449f, 0.15f, 0.0);
        gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // small triangle two
        gl.glPushMatrix();
        gl.glColor3f(0.745f,0.412f,0.282f);
        gl.glTranslated(-1.313f, 0.507f, 0.0f);
        gl.glRotatef(135, 0.0f, 0.0f, 1.0f);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // medium triangle
        gl.glPushMatrix();
        gl.glColor3f(0.651f,0.58f,0.573f);
        gl.glTranslated(0.605f, -0.3f, 0.0f);
        gl.glRotatef(45, 0.0f, 0.0f, 1.0f);
        gl.glScalef(4.26f, 4.26f, 4.26f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // big triangle one
        gl.glPushMatrix();
        gl.glTranslated(-0.05, 0.73f, 0.0);
        gl.glRotatef(135, 0.0f, 0.0f, 1.0f);
        gl.glScalef(6.0f, 6.0f, 6.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // big triangle two
        gl.glPushMatrix();
        gl.glColor3f(0.447f,0.357f,0.38f);
        gl.glTranslated(0.8, 0.73, 0.0);
        gl.glRotatef(315, 0.0f, 0.0f, 1.0f);
        gl.glScalef(6.0f, 6.0f, 6.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();
    }

    public static void dog(GL2 gl, int traverse){

        // square
        gl.glPushMatrix();
        if (traverse == 0) {
            gl.glColor3f(1.0f, 0.0f, 0.0f);
            gl.glTranslated(0.0, 0.0, 0.002);
        } else {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glTranslated(1.1, 0.63, 0.0);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        gl.glRotatef(45, 0.0f, 0.0f, 1.0f);
        Shapes_Tangram.drawSquare(gl, 0.2);
        gl.glPopMatrix();

        // parallelogram
        gl.glPushMatrix();
        if (traverse == 1) {
            gl.glColor3f(1.0f, 0.0f, 0.0f);
            gl.glTranslated(0.0, 0.0, 0.002);
        } else {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glTranslated(-1.0, -0.45, 0.0);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
        gl.glRotatef(180, 1.0f, 1.0f, 0.0f);
        Shapes_Tangram.drawParallelogram(gl, 0.2);
        gl.glPopMatrix();


        // small triangle one
        gl.glPushMatrix();
        if (traverse == 2) {
            gl.glColor3f(1.0f, 0.0f, 0.0f);
            gl.glTranslated(0.0, 0.0, 0.002);
        } else {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glTranslated(1.535, .84, 0.0);
        gl.glRotatef(315, 0.0f, 0.0f, 1.0f);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // small triangle two
        gl.glPushMatrix();
        if (traverse == 3) {
            gl.glColor3f(1.0f, 0.0f, 0.0f);
            gl.glTranslated(0.0, 0.0, 0.002);
        } else {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glTranslated(-0.185, -1.315, 0.0);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        gl.glRotatef(225, 0.0f, 0.0f, 1.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // middle triangle
        gl.glPushMatrix();
        if (traverse == 4) {
            gl.glColor3f(1.0f, 0.0f, 0.0f);
            gl.glTranslated(0.0, 0.0, 0.002);
        } else {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glTranslated(1.085, -1.022, 0.0);
        gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
        gl.glScalef(4.26f, 4.26f, 4.26f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // big triangle one
        gl.glPushMatrix();
        if (traverse == 5) {
            gl.glColor3f(1.0f, 0.0f, 0.0f);
            gl.glTranslated(0.0, 0.0, 0.002);
        } else {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glTranslated(-0.1, -0.4, 0.0);
        gl.glRotatef(180, 0.0f, 0.0f, 1.0f);
        gl.glScalef(6.0f, 6.0f, 6.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // big triangle two
        gl.glPushMatrix();
        if (traverse == 6) {
            gl.glColor3f(1.0f, 0.0f, 0.0f);
            gl.glTranslated(0.0, 0.0, 0.002);
        } else {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glTranslated(1.0, -0.5, 0.0);
        gl.glRotatef(0, 0.0f, 0.0f, 1.0f);
        gl.glScalef(6.0f, 6.0f, 6.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();
    }

    public static void dogSolution(GL2 gl){

        // square
        gl.glPushMatrix();
        gl.glColor3f(0.667f, 0.239f, 0.176f);
        gl.glTranslated(1.1, 0.63, 0.0);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        gl.glRotatef(45, 0.0f, 0.0f, 1.0f);
        Shapes_Tangram.drawSquare(gl, 0.2);
        gl.glPopMatrix();

        // parallelogram
        gl.glPushMatrix();
        gl.glColor3f(0.333f, 0.141f, 0.114f);
        gl.glTranslated(-1.0, -0.45, 0.0);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
        gl.glRotatef(180, 1.0f, 1.0f, 0.0f);
        Shapes_Tangram.drawParallelogram(gl, 0.2);
        gl.glPopMatrix();


        // small triangle one
        gl.glPushMatrix();
        gl.glColor3f(0.745f, 0.412f, 0.282f);
        gl.glTranslated(1.535, .84, 0.0);
        gl.glRotatef(315, 0.0f, 0.0f, 1.0f);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // small triangle two
        gl.glPushMatrix();
        gl.glColor3f(0.745f, 0.412f, 0.282f);
        gl.glTranslated(-0.185, -1.315, 0.0);
        gl.glScalef(3.0f, 3.0f, 3.0f);
        gl.glRotatef(225, 0.0f, 0.0f, 1.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // middle triangle
        gl.glPushMatrix();
        gl.glColor3f(0.651f, 0.580f, 0.573f);
        gl.glTranslated(1.085, -1.022, 0.0);
        gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
        gl.glScalef(4.26f, 4.26f, 4.26f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // big triangle one
        gl.glPushMatrix();
        gl.glColor3f(0.447f, 0.357f, 0.38f);
        gl.glTranslated(-0.1, -0.4, 0.0);
        gl.glRotatef(180, 0.0f, 0.0f, 1.0f);
        gl.glScalef(6.0f, 6.0f, 6.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();

        // big triangle two
        gl.glPushMatrix();
        gl.glColor3f(0.447f, 0.357f, 0.38f);
        gl.glTranslated(1.0, -0.5, 0.0);
        gl.glRotatef(0, 0.0f, 0.0f, 1.0f);
        gl.glScalef(6.0f, 6.0f, 6.0f);
        Shapes_Tangram.drawTriangle(gl, 0.2);
        gl.glPopMatrix();
    }

}

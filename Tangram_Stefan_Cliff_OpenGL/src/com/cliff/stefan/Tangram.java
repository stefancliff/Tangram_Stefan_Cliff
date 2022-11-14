package com.cliff.stefan;
// Stefan Cliff 2019/230449 June 22;

import com.jogamp.opengl.*;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.glu.GLU;
import com.sun.opengl.util.BufferUtil;

// Stefan Cliff 2019/230449 June 22;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Stefan Cliff 2019/230449 June 22;
public class Tangram implements GLEventListener, KeyListener, MouseListener {

	private boolean perspective;

	// light checkboxes
	private JCheckBox lightOnOff;
	private JCheckBox ambientLighting;
	private JCheckBox diffuseLighting;
	private JCheckBox specularLighting;
	private JCheckBox ambientLight;

	// gameplay buttons
	private JButton addButton;
	private JButton removeButton;
	private JButton finishButton;
	private JButton helpButton;
	private JButton quitButton;
	private JButton newGameButton;

	private JLabel label;
	private JFrame frame;
	private GLCanvas canvas;
	private FPSAnimator animator;
	private static final int FPS = 60;
	private int windowWidth = 640;
	private int windowHeight = 480;
	private static final String TITLE = "Stefan Cliff Tangram 2019/230449 June";

	private GLU glu;
	private TextRenderer textRenderer;

	private int randomBlueprint;

	private static final int CAT = 1;
	private static final int BIRD = 2;
	private static final int DOG = 3;


	// initialize a name ID for picking the shapes
	private int nameID;

	// represents which shape is placed into the part of the outline
	private int idSquare 				= 0;
	private int idParallelogram 		= 0;
	private int idSmallTriangle1 		= 0;
	private int idSmallTriangle2 		= 0;
	private int idMediumTriangle 		= 0;
	private int idBigTriangle1			= 0;
	private int idBigTriangle2 			= 0;

	private int traverse = -1; // shows which part of the outline is selected, for the object to be placed (red line)

	private float scaleDelta = 0.01f;

	private static final int SQUARE_ID = 8;
	private static final int SMALL_TRIANGLE_ID = 9;
	private static final int MEDIUM_TRIANGLE_ID = 10;
	private static final int BIG_TRIANGLE_ID = 11;
	private static final int PARALLELOGRAM_ID = 12;

	private final static int TOTAL_NUM_OF_SHAPES = 7;
	private final static int TOTAL_NUM_OF_BLUEPRINTS = 3;

	private static final int BUFFSIZE = 512;
	private IntBuffer selectBuffer;

	private boolean inSelectionMode = false;
	private int xCursor = 0;
	private int yCursor = 0;

	private int currentAngleOfVisibleField = 60; // FOV

	private int angleDelta = 1;

	private float aspectP; // aspect ratio for the palette
	private float aspectB; // aspect ratio for the blueprint
	private float aspectS; // aspect ratio for the solution

	private boolean gameFinished = false;
	private boolean newGame = true;

	private float scaleSquare = 1.0f;
	private float scaleParallelogram = 1.0f;
	private float scaleSmallTriangle1 = 1.0f;
	private float scaleSmallTriangle2 = 1.0f;
	private float scaleMediumTriangle = 1.0f;
	private float scaleBigTriangle1 = 1.0f;
	private float scaleBigTriangle2 = 1.0f;

	private int angleSquare = 90;
	private int  angleParallelogram = 90;
	private int  angleSmallTriangle1 = 90;
	private int  angleSmallTriangle2 = 90;
	private int  angleMediumTriangle = 90;
	private int  angleBigTriangle1 = 90;
	private int  angleBigTriangle2 = 90;

	// parameters for the blueprint itself
	private double translateX;
	private double translateY;
	private float scale;
	private int angleBlueprintX;
	private int angleBlueprintY;
	private int angleBlueprintZ;

	public static void main(String[] args) {
		new Tangram();
	}

	public Tangram() {

		// get the opengl profile context
		GLProfile profile = GLProfile.getDefault();

		GLCapabilities caps = new GLCapabilities(profile);
		caps.setAlphaBits(8);
		caps.setDepthBits(24);
		caps.setDoubleBuffered(true); // needed for reducing flicking and having a smooth animation
		caps.setStencilBits(8);

		SwingUtilities.invokeLater(() -> {

			// create the opengl rendering canvas
			canvas = new GLCanvas();
			canvas.setPreferredSize(new Dimension(windowWidth, windowHeight)); // sets the default dimensions of the window
			canvas.addGLEventListener(this);
			canvas.addMouseListener(this);
			canvas.addKeyListener(this);
			canvas.setFocusable(true);
			canvas.requestFocus();
			canvas.requestFocusInWindow();

			animator = new FPSAnimator(canvas, FPS, true);

			frame = new JFrame();

			removeButton = new JButton("Remove");
			addButton = new JButton("Add");
			finishButton = new JButton("Finish");
			helpButton = new JButton("Help");
			newGameButton = new JButton("New Game");
			quitButton = new JButton("Quit");

			removeButton.setPreferredSize(new Dimension(100, 20));
			addButton.setPreferredSize(new Dimension(100, 20));
			finishButton.setPreferredSize(new Dimension(100, 20));
			helpButton.setPreferredSize(new Dimension(100, 20));
			newGameButton.setPreferredSize(new Dimension(100, 20));
			quitButton.setPreferredSize(new Dimension(100, 20));

			label = new JLabel("Click on the help button to read game instructions.");

			lightOnOff = new JCheckBox("Turn Light ON/OFF", true);
			ambientLighting = new JCheckBox("Ambient Light", false);
			specularLighting = new JCheckBox("Specular Light", false);
			diffuseLighting = new JCheckBox("Diffused Light", false);
			ambientLight = new JCheckBox("Global Ambient Light", false);

			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2,2));

			JPanel row1 = new JPanel();
			row1.add(removeButton);
			row1.add(addButton);

			row1.add(ambientLight);
			row1.add(lightOnOff);
			row1.add(ambientLighting);
			row1.add(diffuseLighting);
			row1.add(specularLighting);
			panel.add(row1);

			JPanel row2 = new JPanel();
			row2.add(label);
			row2.add(helpButton);
			row2.add(finishButton);
			row2.add(newGameButton);
			row2.add(quitButton);
			panel.add(row2);

			frame.add(panel, BorderLayout.SOUTH);

			ambientLight.setFocusable(false);
			lightOnOff.setFocusable(false);
			ambientLighting.setFocusable(false);
			diffuseLighting.setFocusable(false);
			specularLighting.setFocusable(false);

			addButton.addActionListener(e -> {
				if (e.getSource() == addButton) {
					switch (traverse) {
						case 0:
							idSquare = nameID;
							break;

						case 1:
							idParallelogram = nameID;
							break;

						case 2:
							idSmallTriangle1 = nameID;
							break;

						case 3:
							idSmallTriangle2 = nameID;
							break;
						case 4:
							idMediumTriangle = nameID;
							break;

						case 5:
							idBigTriangle1 = nameID;
							break;

						case 6:
							idBigTriangle2 = nameID;
							break;

					}
				}
				addButton.setFocusable(false);
			});

			// 0 means empty
			removeButton.addActionListener(e -> {
				if (e.getSource() == removeButton) {
					if (traverse == 0) {
						idSquare = 0;
						scaleSquare = 1.0f;
						angleSquare = 0;
					} else if (traverse == 1) {
						idParallelogram = 0;
						scaleParallelogram = 1.0f;
						angleParallelogram = 0;
					} else if (traverse == 2) {
						idSmallTriangle1 = 0;
						scaleSmallTriangle1 = 1.0f;
						angleSmallTriangle1 = 0;
					} else if (traverse == 3) {
						idSmallTriangle2 = 0;
						scaleSmallTriangle2 = 1.0f;
						angleSmallTriangle2 = 0;
					} else if (traverse == 4) {
						idMediumTriangle = 0;
						scaleMediumTriangle = 1.0f;
						angleMediumTriangle = 0;
					} else if (traverse == 5) {
						idBigTriangle1 = 0;
						scaleBigTriangle1 = 1.0f;
						angleBigTriangle1 = 0;
					} else if (traverse == 6) {
						idBigTriangle2 = 0;
						scaleBigTriangle2 = 1.0f;
						angleBigTriangle2 = 0;
					}
				}
				removeButton.setFocusable(false);
			});

			finishButton.addActionListener(e -> {
				if (e.getSource() == finishButton) {
					traverse = -1;
					translateX = 0.0;
					translateY = -0.7;
					scale = 0.7f;
					angleBlueprintX = 0;
					angleBlueprintY = 0;
					angleBlueprintZ = 0;
					gameFinished = true;
				}
				finishButton.setFocusable(false);
			});

			helpButton.addActionListener(e -> {
				if (e.getSource() == helpButton) {
					JOptionPane.showMessageDialog(frame,
							"Add Button - after selecting a shape from the palette, you can add it to the selected blueprint shape by the Add button\n" +
									"Remove Button - after selecting a shape from the palette, you can remove it from the selected blueprint shape by the Remove button\n" +
									"Finish Button - after the game finished, by pressing on the finish button, you can see your results\n" +
									"New Game Button - generate a new game\n" +
									"Quit Button - quit from the game \n" +
									"Light - you can enable/disable different light models by checking/unchecking  the light chekboxes (global ambient light, ambient, diffuse and specular)\n\n" +
									"\nShape Controls:\n" +
									"W - traverse through the blueprint\n" +
									"Arrow Up - increase the scale of the shape inserted into the blueprint\n" +
									"Arrow Down - reduce the scale of the shape inserted into the blueprint\n" +
									"Arrow Right - rotate the shape inserted into the blueprint to the right\n" +
									"Arrow Left - rotate the shape inserted into the blueprint to the left\n" +

									"\nBlueprint Controls:\n" +
									"P - Change between perspective and orthographic projection\n" +
									"I - move the blueprint (translate) up\n" +
									"J - move the blueprint (translate) right\n" +
									"K - move the blueprint (translate) down\n" +
									"L - move the blueprint (translate) left\n" +

									"\nNumpad + - zoom in\n" +
									"Numpad - - zoom out\n" +
									"Numpad 1 - rotate blueprint positively around X axis\n" +
									"Numpad 3 - rotate blueprint negatively around X axis\n" +
									"Numpad 4 - rotate blueprint positively around Y axis\n" +
									"Numpad 6 - rotate blueprint negatively around Y axis\n" +
									"Numpad 7 - rotate blueprint positively around Z axis\n" +
									"Numpad 9 - rotate blueprint negatively around Z axis\n"

							, "Help", JOptionPane.INFORMATION_MESSAGE);
				}
				helpButton.setFocusable(false);
			});

			quitButton.addActionListener(e -> {
				if (e.getSource() == quitButton) {
					animator.stop();
					System.exit(0);
				}
				quitButton.setFocusable(false);
			});

			newGameButton.addActionListener(e -> {
				if (e.getSource() == newGameButton) {
					newGame = true;
					gameFinished = false;
				}
				newGameButton.setFocusable(false);
			});

			frame.getContentPane().add(canvas);
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					new Thread(() -> {
						if (animator.isStarted()) animator.stop();
						System.exit(0);
					}).start();
				}
			});
			frame.setTitle(TITLE);
			frame.pack();
			frame.setVisible(true);
			animator.start();
		});
	}
	private void lights(GL2 gl) {
		gl.glColor3d(0.5, 0.5, 0.5);

		float[] zero = {0,0,0,1};

		if (ambientLight.isSelected()) {
			gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[] {0.1f, 0.1f, 0.1f, 1}, 0);
		} else {
			gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, zero, 0);
		}

		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, zero, 0);

		if (lightOnOff.isSelected()) {
			gl.glDisable(GL2.GL_LIGHTING);
		} else {
			gl.glEnable(GL2.GL_LIGHTING);
		}

		float[] ambient = {0.1f, 0.1f, 0.1f, 1};
		float[] diffuse = {1.0f, 1.0f, 1.0f, 1.0f};
		float[] specular = {1.0f, 1.0f, 1.0f, 1.0f};

		if (ambientLighting.isSelected()) {
			gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, ambient, 0);
			gl.glEnable(GL2.GL_LIGHT0);
		} else {
			gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, zero, 0);
			gl.glDisable(GL2.GL_LIGHT0);
		}

		if (diffuseLighting.isSelected()) {
			gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, diffuse, 0);
			gl.glEnable(GL2.GL_LIGHT1);
		} else {
			gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, zero, 0);
			gl.glDisable(GL2.GL_LIGHT1);
		}

		if (specularLighting.isSelected()) {
			float[] shininess = {5.0f};
			gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, specular, 0);
			gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, shininess, 0);
			gl.glEnable(GL2.GL_LIGHT2);
		} else {
			gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, zero, 0);
			gl.glDisable(GL2.GL_LIGHT2);
		}

		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, zero, 0);
	}

	@Override
	public void init(GLAutoDrawable drawable) {

		GL2 gl = drawable.getGL().getGL2();

		gl.glClearColor(1.0f, 1.0f, 1.0f, 0);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_NORMALIZE);
		gl.glEnable(GL2.GL_COLOR_MATERIAL);

		gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, 1);
		gl.glMateriali(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 100);

		float[] ambient = {0.1f, 0.1f, 0.1f, 1.0f};
		float[] diffuse = {1.0f, 1.0f, 1.0f, 1.0f};
		float[] specular = {1.0f, 1.0f, 1.0f, 1.0f};

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPECULAR, specular, 0);

		gl.glClearDepth(1.0f);
		gl.glDepthFunc(GL2.GL_LEQUAL);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		gl.glShadeModel(GL2.GL_SMOOTH);

		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

		glu = GLU.createGLU();

		textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 15));

	}

	@Override
	public void display(GLAutoDrawable drawable) {

		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		// determines if the rest of the game should be rendered or if the user is picking
		if (inSelectionMode) {
			pickModels(drawable);
		} else {
			palette(drawable);
			drawSolution(drawable);
			drawBlueprint(drawable);
		}

		lights(gl);
		float[] zero = {0,0,0,1};

		if (ambientLight.isSelected()) {
			gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[] {0.1f, 0.1f, 0.1f, 1}, 0);
		} else {
			gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, zero, 0);
		}
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, new float[]{0.1F, 0.1F, 0.1F, 1}, 0);

		if (gameFinished) {
			printResult();
		}
		if (!gameFinished) {
			printMatch();
		}

		if (newGame) {
			newGame();
			newGame = false;
		}

	}

	private void pickModels(GLAutoDrawable drawable) {

		GL2 gl = drawable.getGL().getGL2();

		startPicking(drawable);
		palettePicking(drawable);

		gl.glPushName(SQUARE_ID);
		paletteSquare(drawable);
		gl.glPopName();

		gl.glPushName(SMALL_TRIANGLE_ID);
		paletteSmallTriangle(drawable);
		gl.glPopName();

		gl.glPushName(MEDIUM_TRIANGLE_ID);
		paletteMediumTriangle(drawable);
		gl.glPopName();

		gl.glPushName(BIG_TRIANGLE_ID);
		paletteBigTriangle(drawable);
		gl.glPopName();

		gl.glPushName(PARALLELOGRAM_ID);
		paletteParallelogram(drawable);
		gl.glPopName();

		endPicking(drawable);

	}

	private void startPicking(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		selectBuffer = BufferUtil.newIntBuffer(BUFFSIZE);
		gl.glSelectBuffer(BUFFSIZE, selectBuffer);
		gl.glRenderMode(GL2.GL_SELECT);
		gl.glInitNames();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}

	public void palettePicking(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();

		int[] viewport = new int[4];
		float[] projectionMatrix = new float[16];

		gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
		viewport[0] = 0;
		viewport[1] = 0;
		viewport[2] = windowWidth;
		viewport[3] = windowHeight/4;

		gl.glGetFloatv(GL2.GL_PROJECTION_MATRIX, projectionMatrix, 0);

		glu.gluPickMatrix(xCursor,
				(windowHeight - yCursor),
				1.0,
				1.0,
				viewport,
				0);
		gl.glMultMatrixf(projectionMatrix, 0);
		aspectP = ((float) windowHeight/4) / windowWidth;
		gl.glOrtho((float) -10/2,
				(float) 10/2,
				(-10*aspectP)/2,
				(10*aspectP)/2,
				1, 11);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(0.0,   0.0,   5.0,
				0.0, 0.0, 0.0,
				0.0, 1.0, 0.0);

	}

	private void endPicking(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glFlush();

		int numHits = gl.glRenderMode(GL2.GL_RENDER);
		processHits(numHits);
		inSelectionMode = false;
	}

	public void processHits(int numHits) {

		if (numHits == 0) {
			return;
		}

		int selectedNameId = 0;
		float smallestZ = -1.0f;
		boolean isFirstLoop = true;
		int offset = 0;

		for (int i = 0; i < numHits; i++) {
			int numNames = selectBuffer.get(offset);
			offset++;

			float minZ = getDepth(offset);
			offset++;

			// store the smallest Z value
			if (isFirstLoop) {
				smallestZ = minZ;
				isFirstLoop = false;
			} else {
				if (minZ < smallestZ) {
					smallestZ = minZ;
				}
			}

			float maxZ = getDepth(offset);
			offset++;

			for (int j = 0; j < numNames; j++) {

				nameID = selectBuffer.get(offset);
				System.out.println(idToString(nameID) + "\n");

				if (j == (numNames - 1)) {
					if (smallestZ == minZ) {
						selectedNameId = nameID;
					}
				}
				offset++;

			}
		}
	}

	private String idToString(int nameID) {
		if (nameID == SQUARE_ID)
			return "square";
		else if (nameID == SMALL_TRIANGLE_ID)
			return "small_triangle";
		else if (nameID == MEDIUM_TRIANGLE_ID)
			return "medium_triangle";
		else if (nameID == BIG_TRIANGLE_ID)
			return "big_triangle";
		else if (nameID == PARALLELOGRAM_ID)
			return "parallelogram";
		return "None";
	}

	private float getDepth(int offset) {
		long depth = selectBuffer.get(offset);
		return (1.0f + ((float) depth / 0x7fffffff)); // between 0 and 1
	}

	private void palette(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();


		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		gl.glViewport(0, 0, windowWidth, windowHeight/4);
		aspectP = ((float) windowHeight/4) / windowWidth;
		gl.glOrtho((float) -10 / 2,
				(float) 10 / 2,
				(-10 * aspectP) / 2,
				(10 * aspectP) / 2,
				1,
				11);

		gl.glMatrixMode(GL2.GL_MODELVIEW);

		paletteBackground(drawable);

		glu.gluLookAt(0, 0,   5.0,
				0.0, 0.0, 0.0,
				0.0, 1.0, 0.0);

		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

		paletteSquare(drawable);
		paletteSmallTriangle(drawable);
		paletteMediumTriangle(drawable);
		paletteBigTriangle(drawable);
		paletteParallelogram(drawable);

	}

	private void paletteBackground(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glPushMatrix();

		gl.glColor3f(0.0f, 0.0f, 0.0f);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
		gl.glLineWidth(1);

		gl.glTranslated(0.0f, 0.0f, -10f);
		gl.glScalef(8.5f, 1.0f, 0.0f);

		Shapes_Tangram.drawSquare(gl, 1.1);

		gl.glPopMatrix();

		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

	}

	private void paletteSquare(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glColor3f(0.667f, 0.239f, 0.176f);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

		gl.glPushMatrix();
		gl.glTranslated(-3.5f, 0.0f, 0.0f);
		Shapes_Tangram.drawSquare(gl, 0.7);
		gl.glPopMatrix();

	}

	private void paletteParallelogram(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glColor3f(0.333f, 0.141f, 0.114f);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

		gl.glPushMatrix();
		gl.glTranslated(0.9f, 0.0f, 0);
		Shapes_Tangram.drawParallelogram(gl, 0.7);
		gl.glPopMatrix();

	}

	private void paletteSmallTriangle(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glColor3f(0.745f, 0.141f, 0.282f);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

		gl.glPushMatrix();
		gl.glTranslated(-2.3f, -0.15f, 0.0f);
		Shapes_Tangram.drawTriangle(gl, 0.7);
		gl.glPopMatrix();

	}

	private void paletteMediumTriangle(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glColor3f(0.651f, 0.58f, 0.573f);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

		gl.glPushMatrix();
		gl.glTranslated(-1.3f, -0.15f, 0.0f);
		Shapes_Tangram.drawTriangle(gl, 0.7);
		gl.glPopMatrix();

	}

	private void paletteBigTriangle(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glColor3f(0.447f, 0.357f, 0.38f);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

		gl.glPushMatrix();
		gl.glTranslated(-0.3f, -0.15f, 0.0f);
		Shapes_Tangram.drawTriangle(gl, 0.7);
		gl.glPopMatrix();

	}

	private void drawBlueprint(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glViewport(windowWidth/6, windowHeight/4, 5*(windowWidth/6), 3*(windowHeight/4));
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		aspectB = (float) (3*(windowHeight/4)) / ((float) (5*(windowWidth/6)));

		if (perspective) {
			glu.gluPerspective(currentAngleOfVisibleField, 1.f * windowWidth / windowHeight, 1, 100);
		} else {
			gl.glOrtho((float) -10 / 2,
					(float) 10 / 2,
					(-10 * aspectB) / 2,
					(10 * aspectB) / 2,
					1,
					100);
		}
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(0, 0f, 4f,
				0, 0,  0,
				0, 1,  0);

		gl.glColor3f(0.0f, 0.0f, 0.0f);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
		gl.glLineWidth(1f);

		gl.glPushMatrix();

		gl.glTranslated(translateX, translateY, 0.0);

		gl.glScalef(scale, scale, scale);
		gl.glRotatef(angleBlueprintX, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(angleBlueprintY, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(angleBlueprintZ, 0.0f, 0.0f, 1.0f);

		drawOutline(drawable);

		addSquareShape(drawable, idSquare);
		addParallelogramShape(drawable, idParallelogram);
		addSmallTriangleOneShape(drawable, idSmallTriangle1);
		addSmallTriangleTwoShape(drawable, idSmallTriangle2);
		addMediumTriangleShape(drawable, idMediumTriangle);
		addBigTriangleOneShape(drawable, idBigTriangle1);
		addBigTriangleTwoShape(drawable, idBigTriangle2);

		gl.glPopMatrix();

		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

	}

	private void addSquareShape(GLAutoDrawable drawable, int nameID) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glPushMatrix();

		switch (randomBlueprint){

			case CAT:
				gl.glTranslated(-.95f, 0.85f, 0.0f);
				break;

			case BIRD:
				gl.glTranslated(0.0f, 0.0f, 0.0f);
				break;

			case DOG:
				gl.glTranslated(1.1, 0.63, 0.0);
				break;
		}

		gl.glScalef(scaleSquare, scaleSquare, scaleSquare);
		gl.glRotatef(angleSquare, 0.0f, 0.0f, 1.0f);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		addShape(drawable, nameID);

		gl.glPopMatrix();
	}

	private void addParallelogramShape(GLAutoDrawable drawable, int nameID) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glPushMatrix();

		switch (randomBlueprint) {
			case CAT :
				gl.glTranslated(0.972, -0.988, 0.0);
				break;

			case BIRD :
				gl.glTranslated(0.95f, 0.088f, 0.0f);
				gl.glRotatef(180, 1.0f, 0.0f, 0.0f);
				break;

			case DOG:
				gl.glTranslated(-1.0, -0.45, 0.0);
				gl.glRotatef(180, 1.0f, 1.0f, 0.0f);
				break;
		}

		gl.glScalef(scaleParallelogram, scaleParallelogram, scaleParallelogram);
		gl.glRotatef(angleParallelogram, 0.0f, 0.0f, 1.0f);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		addShape(drawable, nameID);

		gl.glPopMatrix();
	}

	private void addSmallTriangleOneShape(GLAutoDrawable drawable, int nameID) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glPushMatrix();

		switch (randomBlueprint) {
			case CAT:
				gl.glTranslated(-1.162f, 1.28f, 0.0);
				break;
			case BIRD:
				gl.glTranslated(-0.449f, 0.15f, 0.0);
				break;
			case DOG:
				gl.glTranslated(1.535, .84, 0.0);
				break;
		}

		gl.glScalef(scaleSmallTriangle1, scaleSmallTriangle1, scaleSmallTriangle1);
		gl.glRotatef(angleSmallTriangle1, 0.0f, 0.0f, 1.0f);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		addShape(drawable, nameID);

		gl.glPopMatrix();
	}

	private void addSmallTriangleTwoShape(GLAutoDrawable drawable, int nameID) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glPushMatrix();

		switch (randomBlueprint) {
			case CAT:
				gl.glTranslated(-0.737f, 1.28f, 0.0);
				break;
			case BIRD:
				gl.glTranslated(-1.313f, 0.507f, 0.0f);
				break;
			case DOG:
				gl.glTranslated(-0.185, -1.315, 0.0);
				break;
		}

		gl.glScalef(scaleSmallTriangle2, scaleSmallTriangle2, scaleSmallTriangle2);
		gl.glRotatef(angleSmallTriangle2, 0.0f, 0.0f, 1.0f);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		addShape(drawable, nameID);

		gl.glPopMatrix();
	}

	private void addMediumTriangleShape(GLAutoDrawable drawable, int nameID) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glPushMatrix();

		switch (randomBlueprint) {
			case CAT:
				gl.glTranslated(-0.737f, 0.204f, 0.0f);
				break;
			case BIRD:
				gl.glTranslated(0.605f, -0.3f, 0.0f);
				break;
			case DOG:
				gl.glTranslated(1.085, -1.022, 0.0);
				break;
		}

		gl.glScalef(scaleMediumTriangle, scaleMediumTriangle, scaleMediumTriangle);
		gl.glRotatef(angleMediumTriangle, 0.0f, 0.0f, 1.0f);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		addShape(drawable, nameID);

		gl.glPopMatrix();
	}

	private void addBigTriangleOneShape(GLAutoDrawable drawable, int nameID) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glPushMatrix();

		switch (randomBlueprint) {
			case CAT:
				gl.glTranslated(-0.091f, 0.0, 0.0);
				break;
			case BIRD:
				gl.glTranslated(-0.05, 0.73f, 0.0);
				break;
			case DOG:
				gl.glTranslated(-0.1, -0.4, 0.0);
				break;
		}

		gl.glScalef(scaleBigTriangle1, scaleBigTriangle1, scaleBigTriangle1);
		gl.glRotatef(angleBigTriangle1, 0.0f, 0.0f, 1.0f);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		addShape(drawable, nameID);

		gl.glPopMatrix();
	}

	private void addBigTriangleTwoShape(GLAutoDrawable drawable, int nameID) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glPushMatrix();

		switch (randomBlueprint) {
			case CAT:
				gl.glTranslated(0.035f, -0.9f, 0.0);
				break;

			case BIRD:
				gl.glTranslated(0.8, 0.73, 0.0);
				break;

			case DOG:
				gl.glTranslated(1.0, -0.5, 0.0);
				break;
		}

		gl.glScalef(scaleBigTriangle2, scaleBigTriangle2, scaleBigTriangle2);
		gl.glRotatef(angleBigTriangle2, 0.0f, 0.0f, 1.0f);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		addShape(drawable, nameID);

		gl.glPopMatrix();
	}

	private void drawOutline(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glPushMatrix();
		gl.glTranslated(0.0, 0.0, 0.001);
		switch (randomBlueprint) {
			case CAT:
				Animals_Tangram.cat(gl, traverse);
				break;
			case BIRD:
				Animals_Tangram.bird(gl, traverse);
				break;
			case DOG:
				Animals_Tangram.dog(gl, traverse);
				break;
		}
		gl.glPopMatrix();
	}

	public void addShape(GLAutoDrawable drawable, int nameID) {
		GL2 gl = drawable.getGL().getGL2();

		switch (nameID) {
			case SQUARE_ID:
			{
				gl.glColor3f(0.667f,0.239f,0.176f);
				Shapes_Tangram.drawSquare(gl, 0.2);
				break;
			}
			case PARALLELOGRAM_ID:
			{
				gl.glColor3f(0.333f, 0.141f, 0.114f);
				Shapes_Tangram.drawParallelogram(gl, 0.2);
				break;
			}
			case SMALL_TRIANGLE_ID:
			{
				gl.glColor3f(0.745f, 0.412f, 0.282f);
				Shapes_Tangram.drawTriangle(gl, 0.2);
				break;
			}
			case MEDIUM_TRIANGLE_ID:
			{
				gl.glColor3f(0.651f, 0.580f, 0.573f);
				Shapes_Tangram.drawTriangle(gl, 0.2);
				break;
			}
			case BIG_TRIANGLE_ID:
			{
				gl.glColor3f(0.447f, 0.357f, 0.38f);
				Shapes_Tangram.drawTriangle(gl, 0.2);
				break;
			}
		}
	}

	private void drawSolution(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 3*(windowHeight/4), windowWidth/4, windowHeight/4);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		aspectS = (float) (windowHeight/4) / (float) (windowWidth/4);

		gl.glOrtho((float) -10 / 2,
				(float) 10 / 2,
				(-10 * aspectS) / 2,
				(10 * aspectS) / 2,
				1,
				100);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(0, 0f, 3f,
				0, 0,  0,
				0, 1,  0);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

		gl.glPushMatrix();
		gl.glScalef(1.5f, 1.5f, 1.5f);

		drawSolutionOutline(drawable);
		renderSolutionShape(drawable);

		gl.glPopMatrix();
	}

	private void drawSolutionOutline(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
		gl.glPushMatrix();
		gl.glTranslated(0.0, 0.0, 0.001);
		switch (randomBlueprint) {
			case CAT:
				Animals_Tangram.cat(gl, -1);
				break;
			case BIRD:
				Animals_Tangram.bird(gl, -1);
				break;
			case DOG:
				Animals_Tangram.dog(gl, -1);
				break;
		}
		gl.glPopMatrix();
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
	}

	private void renderSolutionShape(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		switch (randomBlueprint) {
			case CAT:
				Animals_Tangram.catSolution(gl);
				break;

			case BIRD:
				Animals_Tangram.birdSolution(gl);
				break;

			case DOG:
				Animals_Tangram.dogSolution(gl);
				break;
		}
	}

	public void resetValuesToDefault(ArrayList<Integer> list){
		perspective = false;

		randomBlueprint = getRandomElement(list);

		nameID = 0;

		scale = 1;
		translateX = 0.0;
		translateY = 0.0;
		angleBlueprintX = 0;
		angleBlueprintY = 0;
		angleBlueprintZ = 0;
		currentAngleOfVisibleField = 60;

		traverse = -1;

		scaleDelta = 0.01f;
		angleDelta = 1;

		idSquare = 0;
		idParallelogram = 0;
		idSmallTriangle1 = 0;
		idSmallTriangle2 = 0;
		idMediumTriangle = 0;
		idBigTriangle1 = 0;
		idBigTriangle2 = 0;

		scaleSquare = 1.0f;
		angleSquare = 0;
		scaleParallelogram = 1.0f;
		angleParallelogram = 0;
		scaleSmallTriangle1 = 1.0f;
		angleSmallTriangle1 = 0;
		scaleSmallTriangle2 = 1.0f;
		angleSmallTriangle2 = 0;
		scaleMediumTriangle = 1.0f;
		angleMediumTriangle = 0;
		scaleBigTriangle1 = 1.0f;
		angleBigTriangle1 = 0;
		scaleBigTriangle2 = 1.0f;
		angleBigTriangle2 = 0;

	}
	public void newGame() {

		// mechanism for choosing the animal randomly
		ArrayList<Integer> list = new ArrayList<>();
		for (int i = 1; i <= TOTAL_NUM_OF_BLUEPRINTS; i++) {
			list.add(i);
		}
		resetValuesToDefault(list);

	}

	public int getRandomElement(List<Integer> list) {
		Random rand = new Random();
		return list.get(rand.nextInt(list.size()));
	}

	public void writeText(String text, int x, int y) {
		textRenderer.beginRendering(windowWidth, windowHeight);
		textRenderer.setColor(0.0f, 0.0f, 0.0f, 1);
		textRenderer.draw(text, x, y);
		textRenderer.endRendering();
	}

	public void printMatch() {

		switch (traverse) {
			case 0:
				if (idSquare == SQUARE_ID) {
					if (squareScaleCheck(scaleSquare).equals("appropriate") && squareRotationCheck(randomBlueprint, angleSquare).equals("correct")) {
						writeText("Well done! Correct shape, rotation and scaling.", (int) (windowWidth / 4f), windowHeight - 40);
					}
				} break;

			case 1:
				if (idParallelogram == PARALLELOGRAM_ID) {
					if (parallelogramScaleCheck(scaleParallelogram).equals("appropriate") && parallelogramRotationCheck(randomBlueprint, angleParallelogram).equals("correct")) {
						writeText("Well done! Correct shape, rotation and scaling.", (int)(windowWidth / 4f), windowHeight-40);
					}
				} break;


			case 2:
				if (idSmallTriangle1 == SMALL_TRIANGLE_ID) {
					if (smallTriangleOneScaleCheck(scaleSmallTriangle1).equals("appropriate") && smallTriangleOneRotationCheck(randomBlueprint, angleSmallTriangle1).equals("correct")) {
						writeText("Well done! Correct shape, rotation and scaling.", (int)(windowWidth / 4f), windowHeight-40);
					}
				} break;


			case 3:
				if (idSmallTriangle2 == SMALL_TRIANGLE_ID) {
					if (smallTriangleTwoScaleCheck(scaleSmallTriangle2).equals("appropriate") && smallTriangleTwoRotationCheck(randomBlueprint, angleSmallTriangle2).equals("correct")) {
						writeText("Well done! Correct shape, rotation and scaling.", (int)(windowWidth / 4f), windowHeight-40);
					}
				} break;


			case 4:
				if (idMediumTriangle == MEDIUM_TRIANGLE_ID) {
					if (mediumTriangleScaleCheck(scaleMediumTriangle).equals("appropriate") && mediumTriangleRotationCheck(randomBlueprint, angleMediumTriangle).equals("correct")) {
						writeText("Well done! Correct shape, rotation and scaling.", (int)(windowWidth / 4f), windowHeight-40);
					}
				} break;

			case 5:
				if (idBigTriangle1 == BIG_TRIANGLE_ID) {
					if (bigTriangleOneScaleCheck(scaleBigTriangle1).equals("appropriate") && bigTriangleOneRotationCheck(randomBlueprint, angleBigTriangle1).equals("correct")) {
						writeText("Well done! Correct shape, rotation and scaling.", (int)(windowWidth / 4f), windowHeight-40);
					}
				} break;

			case 6:
				if (idBigTriangle2 == BIG_TRIANGLE_ID) {
					if (bigTriangleTwoScaleCheck(scaleBigTriangle2).equals("appropriate") && bigTriangleTwoRotationCheck(randomBlueprint, angleBigTriangle2).equals("correct")) {
						writeText("Well done! Correct shape, rotation and scaling.", (int)(windowWidth / 4f), windowHeight-40);
					}
				} break;
		}
	}

	public void printResult() {
		writeText("RESULT: " + matchedShape() + "/7 shapes matched correctly", (int) (windowWidth / 5f), windowHeight-40);

		writeText("Square - |" + "| Matched shape: || " + idToString(idSquare) + " || Scaling: || " + squareScaleCheck(scaleSquare) + " || Rotation: || " + squareRotationCheck(randomBlueprint, angleSquare) + " ||",
				(int) (windowWidth / 5f), windowHeight - 60);
		writeText("Parallelogram - |" + "| Matched shape: || " + idToString(idParallelogram) + " || Scaling: || " + parallelogramScaleCheck(scaleParallelogram) + " || Rotation: || " + parallelogramRotationCheck(randomBlueprint, angleParallelogram) + " ||",
				(int) (windowWidth / 5f), windowHeight - 80);
		writeText("Small triangle one - |" + "| Matched shape: || " + idToString(idSmallTriangle1) + " || Scaling: || " + smallTriangleOneScaleCheck(scaleSmallTriangle1) + " || Rotation: || " + smallTriangleOneRotationCheck(randomBlueprint, angleSmallTriangle1) + " ||",
				(int) (windowWidth / 5f), windowHeight - 100);
		writeText("Small triangle two - |" + "| Matched shape: || " + idToString(idSmallTriangle2) + " || Scaling: || " + smallTriangleTwoScaleCheck(scaleSmallTriangle2) + " || Rotation: || " + smallTriangleTwoRotationCheck(randomBlueprint, angleSmallTriangle2) + " ||",
				(int) (windowWidth / 5f), windowHeight - 120);
		writeText("Medium triangle - |" + "| Matched shape: || " + idToString(idMediumTriangle) + " || Scaling: || " + mediumTriangleScaleCheck(scaleMediumTriangle) + " || Rotation: || " + mediumTriangleRotationCheck(randomBlueprint, angleMediumTriangle) + " ||",
				(int) (windowWidth / 5f), windowHeight - 140);
		writeText("Big triangle one - |" + "| Matched shape: || " + idToString(idBigTriangle1) + " || Scaling: || " + bigTriangleOneScaleCheck(scaleBigTriangle1) + " || Rotation: || " + bigTriangleOneRotationCheck(randomBlueprint, angleBigTriangle1) + " ||",
				(int) (windowWidth / 5f), windowHeight - 160);
		writeText("Big triangle two - |" + "| Matched shape: || " + idToString(idBigTriangle2) + " || Scaling: || " + bigTriangleTwoScaleCheck(scaleBigTriangle2) + " || Rotation: || " + bigTriangleTwoRotationCheck(randomBlueprint, angleBigTriangle2) + " ||",
				(int) (windowWidth / 5f), windowHeight - 180);
	}

	private int matchedShape() {
		int match = 0;
		if (idSquare 			== SQUARE_ID) 			{ match++; }
		if (idParallelogram 	== PARALLELOGRAM_ID) 	{ match++; }
		if (idSmallTriangle1 	== SMALL_TRIANGLE_ID) 	{ match++; }
		if (idSmallTriangle2 	== SMALL_TRIANGLE_ID) 	{ match++; }
		if (idMediumTriangle 	== MEDIUM_TRIANGLE_ID) 	{ match++; }
		if (idBigTriangle1 		== BIG_TRIANGLE_ID) 	{ match++; }
		if (idBigTriangle2 		== BIG_TRIANGLE_ID) 	{ match++; }
		return match;
	}

	public String squareScaleCheck(float scale) {
		float scaling = (float)(Math.round(scale*100.0f)) / 100.0f;

		String text;
		if (scaling == 3.0f) text = "appropriate";
		else text = "not appropriate";
		return text;
	}

	public String parallelogramScaleCheck(float scale) {
		float scaling = (float)(Math.round(scale*100.0f)) / 100.0f;

		String text;
		if (scaling == 3.0) text = "appropriate";
		else text = "not appropriate";
		return text;
	}

	public String smallTriangleOneScaleCheck(float scale) {
		float scaling = (float)(Math.round(scale*100.0f)) / 100.0f;

		String text;
		if (scaling == 3.0) text = "appropriate";
		else text = "not appropriate";
		return text;
	}

	public String smallTriangleTwoScaleCheck(float scale) {
		float scaling = (float)(Math.round(scale*100.0f)) / 100.0f;

		String text;
		if (scaling == 3.0) text = "appropriate";
		else text = "not appropriate";
		return text;
	}

	public String mediumTriangleScaleCheck(float scale) {
		float scaling = (float)(Math.round(scale*100.0f)) / 100.0f;

		String text;
		if (scaling == 4.26f) text = "appropriate";
		else text = "not appropriate";
		return text;
	}

	public String bigTriangleOneScaleCheck(float scale) {
		float scaling = (float)(Math.round(scale*100.0f)) / 100.0f;

		String text;
		if (scaling == 6.0) text = "appropriate";
		else text = "not appropriate";
		return text;
	}

	public String bigTriangleTwoScaleCheck(float scale) {
		float scaling = (float)(Math.round(scale*100.0f)) / 100.0f;

		String text;
		if (scaling == 6.0) text = "appropriate";
		else text = "not appropriate";
		return text;
	}

	public String squareRotationCheck(int blueprint, int angle) {

		String text;
		if (blueprint == CAT) {
			if (angle == 45 || angle == 135 || angle == 225 || angle == 315 || angle == 360) {
				text = "correct";
			} else {
				text = "incorrect";
			}
		} else if (blueprint == BIRD) {
			if (angle == 0 || angle == 90 || angle == 180 || angle == 360) {
				text = "correct";
			} else {
				text = "incorrect";
			}
		} else if (blueprint == DOG) {
			if (angle == 45 || angle == 90 || angle == 180 || angle == 135 || angle == 225 || angle == 270 || angle == 315 || angle == 360 && idSquare != 0) {
				text = "correct";
			} else {
				text = "incorrect";
			}
		} else {
			text = "incorrect";
		}
		return text;
	}

	public String parallelogramRotationCheck(int blueprint, int angle) {

		String text;
		if (blueprint == CAT) {
			if (angle == 45 || angle == 225) {
				text = "correct";
			} else {
				text = "incorrect";
			}
		} else if (blueprint == BIRD) {
			if (angle == 135 || angle == 270) {
				text = "correct";
			} else {
				text = "incorrect";
			}
		} else if (blueprint == DOG) {
			if (angle == 90 || angle == 180 || angle == 270 || angle == 360) {
				text = "correct";
			} else {
				text = "incorrect";
			}
		} else {
			text = "incorrect";
		}

		return text;
	}

	public String smallTriangleOneRotationCheck(int blueprint, int angle) {

		String text;
		if (blueprint == CAT) {
			if (angle == 45 && idSmallTriangle1 != 0) {
				text = "correct";
			} else {
				text = "incorrect";
			}
		} else if (blueprint == BIRD) {
			if (angle == 90 || angle == 180 || angle == 270 || angle == 360) {
				text = "correct";
			} else {
				text = "incorrect";
			}
		} else if (blueprint == DOG) {
			if (angle == 315) {
				text = "correct";
			} else {
				text = "incorrect";
			}
		} else {
			text = "incorrect";
		}
		return text;
	}

	public String smallTriangleTwoRotationCheck(int blueprint, int angle) {

		String text;
		if (blueprint == CAT) {
			if (angle == 225) {
				text = "correct";
			} else {
				text = "incorrect";
			}
		} else if (blueprint == BIRD) {
			if (angle == 135 || angle == 270) {
				text = "correct";
			} else {
				text = "incorrect";
			}
		} else if (blueprint == DOG) {
			if (angle == 225 && idSmallTriangle2 != 0) {
				text = "correct";
			} else {
				text = "incorrect";
			}
		} else {
			text = "incorrect";
		}
		return text;
	}

	public String mediumTriangleRotationCheck(int blueprint, int angle) {

		String text;
		if (blueprint == CAT) {
			if (angle == 0 || angle == 360) {
				text = "correct";
			} else {
				text = "incorrect";
			}
		} else if (blueprint == BIRD) {
			if (angle == 45 || angle == 90 || angle == 180 || angle == 270 || angle == 360) {
				text = "correct";
			} else {
				text = "incorrect";
			}
		} else if (blueprint == DOG) {
			if (angle == 90) {
				text = "correct";
			} else {
				text = "incorrect";
			}
		} else {
			text = "incorrect";
		}
		return text;
	}

	public String bigTriangleOneRotationCheck(int blueprint, int angle) {

		String text;
		if (blueprint == CAT) {
			if (angle == 45) {
				text = "correct";
			} else {
				text = "incorrect";
			}
		} else if (blueprint == BIRD) {
			if ((angle == 135) && idBigTriangle1 != 0) {
				text = "correct";
			} else {
				text = "incorrect";
			}
		} else if (blueprint == DOG) {
			if (angle == 180) {
				text = "correct";
			} else {
				text = "incorrect";
			}
		} else {
			text = "incorrect";
		}
		return text;
	}

	public String bigTriangleTwoRotationCheck(int blueprint, int angle) {

		String text;
		if (blueprint == CAT) {
			if (angle == 45) {
				text = "correct";
			} else {
				text = "incorrect";
			}
		} else if (blueprint == BIRD) {
			if (angle == 315) {
				text = "correct";
			} else {
				text = "incorrect";
			}
		} else if (blueprint == DOG) {
			if (angle == 0 || angle == 360) {
				text = "correct";
			} else {
				text = "incorrect";
			}
		} else {
			text = "incorrect";
		}
		return text;
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		windowWidth = width;
		windowHeight = height;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {

			/*
				//Used for testing to try and see why I was having issues with my arrow keys
	
				int key = e.getKeyCode();
				char keyText = e.getKeyChar();
				String text = KeyEvent.getKeyText(key);
				System.out.println(" " + keyText);
				System.out.println(key);
				System.out.println(text);
			*/


		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				if(traverse == 0 ) {
					scaleSquare += scaleDelta;
				}
				else if(traverse == 1) {
					scaleParallelogram += scaleDelta;
				}
				else if(traverse == 2) {
					scaleSmallTriangle1 += scaleDelta;
				}
				else if(traverse == 3) {
					scaleSmallTriangle2 += scaleDelta;
				}
				else if(traverse == 4) {
					scaleMediumTriangle += scaleDelta;
				}
				else if (traverse == 5) {
					scaleBigTriangle1 += scaleDelta;
				}
				else if (traverse == 6) {
					scaleBigTriangle2 += scaleDelta;
				}
				break;

				/*
				switch (traverse) {
					case 0:
						scaleSquare += scaleDelta;
						break;

					case 1:
						scaleParallelogram += scaleDelta;
						break;

					case 2:
						scaleSmallTriangle1 += scaleDelta;
						break;

					case 3:
						scaleSmallTriangle2 += scaleDelta;
						break;

					case 4:
						scaleMediumTriangle += scaleDelta;
						break;

					case 5:
						scaleBigTriangle1 += scaleDelta;
						break;

					case 6:
						scaleBigTriangle2 += scaleDelta;
						break;

				}}*/

			case KeyEvent.VK_DOWN:

				if(traverse == 0 ) {
					scaleSquare -= scaleDelta;
				}

				else if(traverse == 1) {
					scaleParallelogram -= scaleDelta;
				}

				else if(traverse == 2) {
					scaleSmallTriangle1 -= scaleDelta;
				}

				else if(traverse == 3){
					scaleSmallTriangle2 -= scaleDelta;
				}

				else if(traverse == 4) {
					scaleMediumTriangle -= scaleDelta;
				}

				else if (traverse == 5) {
					scaleBigTriangle1 -= scaleDelta;
				}

				else if (traverse == 6) {
					scaleBigTriangle2 -= scaleDelta;
				}
				break;


			case KeyEvent.VK_RIGHT:
				if (traverse == 0 ) {
					if (angleSquare > 0 || angleSquare < 360)
					{
						angleSquare -= angleDelta;
					}
				}

				else if (traverse == 1) {
						if (angleParallelogram > 0 || angleParallelogram < 360)
						{
							angleParallelogram -= angleDelta;
						}
				}

				else if (traverse == 2) {
						if (angleSmallTriangle1 > 0 || angleSmallTriangle1 < 360)
						{
							angleSmallTriangle1 -= angleDelta;
						}
				}

				else if (traverse == 3) {
						if (angleSmallTriangle2 > 0 || angleSmallTriangle2 < 360)
						{
							angleSmallTriangle2 -= angleDelta;
						}
				}

				else if (traverse == 4) {
						if (angleMediumTriangle > 0 || angleMediumTriangle < 360)
						{
							angleMediumTriangle -= angleDelta;
						}
				}

				else if (traverse == 5) {
						if (angleBigTriangle1 > 0 || angleBigTriangle1 < 360)
						{
							angleBigTriangle1 -= angleDelta;
						}
				}

				else if (traverse == 6) {
						if (angleBigTriangle2 > 0 || angleBigTriangle2 < 360)
						{
							angleBigTriangle2 -= angleDelta;
						}
				}
				break;


			case KeyEvent.VK_LEFT:
				if(traverse == 0 ) {
					if (angleSquare > 0 || angleSquare < 360)
					{
						angleSquare += angleDelta;
					}
				}

				else if(traverse == 1) {
					if (angleParallelogram > 0 || angleParallelogram < 360)
					{
						angleParallelogram += angleDelta;
					}
				}

				else if(traverse == 2) {
					if (angleSmallTriangle1 > 0 || angleSmallTriangle1 < 360)
					{
						angleSmallTriangle1 += angleDelta;
					}
				}

				else if(traverse == 3) {
					if (angleBigTriangle2 > 0 || angleSmallTriangle2 < 360)
					{
						angleSmallTriangle2 += angleDelta;
					}
				}

				else if(traverse == 4) {
					if (angleMediumTriangle > 0 || angleMediumTriangle < 360)
					{
						angleMediumTriangle += angleDelta;
					}
				}

				else if (traverse == 5) {
					if (angleBigTriangle1 > 0 || angleBigTriangle1 < 360)
					{
						angleBigTriangle1 += angleDelta;
					}
				}

				else if (traverse == 6) {
					if (angleBigTriangle2 > 0 || angleBigTriangle2 < 360)
					{
						angleBigTriangle2 += angleDelta;
					}
				}
				break;


			// THIS IS FOR THE WHOLE BLUEPRINT
			case KeyEvent.VK_ADD:
				scale += 0.1f;
				break;


			case KeyEvent.VK_SUBTRACT:
				scale -= 0.1f;
				break;


			case KeyEvent.VK_I:
				translateY += 0.1f;
				break;


			case KeyEvent.VK_K:
				translateY -= 0.1f;
				break;


			case KeyEvent.VK_J:
				translateX -= 0.1f;
				break;


			case KeyEvent.VK_L:
				translateX += 0.1f;
				break;


			case KeyEvent.VK_NUMPAD1:
				angleBlueprintX += angleDelta;
				break;

			case KeyEvent.VK_NUMPAD3:
				angleBlueprintX -= angleDelta;
				break;

			case KeyEvent.VK_NUMPAD4:
				angleBlueprintY += angleDelta;
				break;

			case KeyEvent.VK_NUMPAD6:
				angleBlueprintY -= angleDelta;
				break;

			case KeyEvent.VK_NUMPAD7:
				angleBlueprintZ += angleDelta;
				break;

			case KeyEvent.VK_NUMPAD9:
				angleBlueprintZ -= angleDelta;
				break;

			case KeyEvent.VK_W:
				if (!gameFinished) {
					traverse++;

					if (traverse == TOTAL_NUM_OF_SHAPES)
						traverse = 0;
				}
				break;


			case KeyEvent.VK_P:
				perspective = !perspective;
				break;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch (e.getButton()) {
			case MouseEvent.BUTTON1:
				xCursor = e.getX();
				yCursor = e.getY();
				inSelectionMode = true;
				break;
			case MouseEvent.BUTTON3:
				break;
		}
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}

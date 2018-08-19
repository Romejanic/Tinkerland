# Tinkerland
An open-source game that's all about tweaks and mods!

# Set up
1. Open a Terminal and execute `$ git clone https://github.com/Romejanic/Tinkerland.git`.
2. Download the required libraries (listed below).

## To use an IDE
3. Open Eclipse, select the cloned repo as the workspace.
4. Create a new project in the cloned repo.
5. Add the `src` and `res` directories as source folders.
6. Add the required libraries to the build path (including LWJGL natives).
7. Open the `Tinkerland.java` file and click Run.

## To use a command line
3. Add the libraries to the classpath.
4. Compile the code (`$ javac src/*.java`)
5. Run the program (`$ java com.tinkerland.Tinkerland`)

# Required Libraries
All version numbers are minimum requirements.
* [LWJGL 3.1.6](http://lwjgl.org/) (required modules are listed below + natives)
  * LWJGL core (lwjgl.jar)
  * GLFW (lwjgl-glfw.jar)
  * OpenGL (lwjgl-opengl.jar)
  * OpenAL (lwjgl-openal.jar)
  * STB (lwjgl-stb.jar)
* [JOML 1.9.4](https://github.com/JOML-CI/JOML/releases/tag/1.9.4)
* [TWL's PNGDecoder](http://twl.l33tlabs.org/dist/PNGDecoder.jar)
* [Google GSON 2.2.4](https://github.com/google/gson/releases)
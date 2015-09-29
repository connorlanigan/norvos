#Contributing
This is an open source project, and we want to keep it this way. That means that any code contributed to this project needs to be licensed accordingly.

If you want to contribute code to this project, you need to agree to allowing us to use your code freely. This includes (but is not limited to) distributing your code.
*You will still keep ownership of the copyright in your contribution.*

To get started, [sign the Contributor License Agreement](https://www.clahub.com/agreements/StarfishInteractive/norvos).

## Code style

#### Using Eclipse
If you are using Eclipse, please import the **Formatter Settings** and the **Cleanup Settings** of this project. They are located in `/Contributing/`.

Before commiting changes, right click the project and select `Source -> Clean Up...`, using the imported settings.


#### Not using Eclipse
For people not using Eclipse, here are some guidelines.
They do **not** include everything stated in the Eclipse Formatter Settings, and using Eclipse is recommended.

 - Keep starting brackets on the same line:
 
 ```
  public static void main() {
    System.out.println("Great!");
  }
 
  Constructor() {
    Runnable r = () -> {
      fField.set(20);
    };
  }
 ```
 - Use tabs to indent your code.

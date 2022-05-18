import main.instr.JASMInstrumentation;

public class JASMinstr{
    public static void main(String[] args){
        String jarName = args[0];
        System.out.println("this static ASM instrumentator");
        new JASMInstrumentation().instr(jarName);
    }
}
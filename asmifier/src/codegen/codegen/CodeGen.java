package codegen.codegen;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;

public class CodeGen {
    public static void main(String[] asgs) throws Exception{
        TestClass t = new TestClass();
        t.a();

        //ASMifier.main(new String[]{"C:\\Users\\user\\IdeaProjects\\JASM_test\\asmifier\\target\\classes\\codegen\\codegen\\TestClass.class"});
        new ClassReader(TestClass.class.getResourceAsStream("TestClass.class"))
                .accept(new TraceClassVisitor(null, new ASMifier(), new PrintWriter(System.out)), 0);

    }

}

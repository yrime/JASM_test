package main.instr;

import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.ASM9;

public class ClassInstrumentation extends ClassVisitor {

    public ClassInstrumentation(ClassVisitor classVisitor) {
        super(ASM9, classVisitor);
    }

    @Override
    public void visit(int i, int i1, String s, String s1, String s2, String[] strings) {
        System.out.println("instrumented class: " + s);
        super.visit(i, i1, s, s1, s2, strings);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        methodVisitor = new MethodTransformer(methodVisitor, access, name, descriptor, signature, exceptions);
        return methodVisitor;
    }

    @Override
    public void visitEnd() {
        System.out.println("class was instrumented");
        super.visitEnd();
    }

    private class MethodTransformer extends MethodVisitor{
        String name;
        public MethodTransformer(MethodVisitor methodVisitor, int access, String name,
                                 String descriptor, String signature, String[] exceptions){
            super(ASM9, methodVisitor);
            this.name = name;
        }

        private void instr(){
            super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            super.visitLdcInsn("this instrumented method " + name);
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }

        @Override
        public void visitCode(){
            super.visitCode();
            instr();
        }
    }
}

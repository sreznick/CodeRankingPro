package coderank.impl.staticanalysis;

import coderank.impl.javagraph.MethodNode;
import coderank.impl.javagraph.Node;
import coderank.impl.graphbuilder.GraphBuilderException;
import coderank.impl.launchers.StaticLauncher;
import jdk.internal.net.http.common.Pair;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.InputStream;
import java.util.HashSet;

public class ClassDescriptor extends ClassVisitor {

    public InputStream className;

    public String actualClassName;

    public String sourceName;

    public Node<MethodNode> sourceNode;

    public ClassDescriptor(InputStream stream) {
        super(Opcodes.ASM7);
        className = stream;
    }

    @Override
    public void visit(int version, int access, String name,
                      String signature, String superName, String[] interfaces) {
        actualClassName = name;
    }

    @Override
    public void visitSource(final String source, final String debug) {
        sourceName = source;
        sourceNode = MethodNode.createNode();
        sourceNode.payload = new MethodNode(sourceName, "");
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc,
                                   String signature, Object value) {
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name,
                                     String desc, String signature, String[] exceptions) {
        String actualName = (actualClassName + '.' + name).replace('/', '.');
        Node<MethodNode> methodNode = MethodNode.createNode();
        methodNode.payload = new MethodNode(actualName, desc);
        try {
            StaticLauncher.loader.applyGetMethodSources().put(methodNode, sourceNode);
        } catch (GraphBuilderException e) {
            e.printStackTrace();
        }
        return new ReferencedMethodsVisitor(methodNode);
    }

}

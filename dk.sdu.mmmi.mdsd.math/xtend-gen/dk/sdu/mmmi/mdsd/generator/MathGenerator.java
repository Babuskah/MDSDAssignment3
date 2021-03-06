/**
 * generated by Xtext 2.25.0
 */
package dk.sdu.mmmi.mdsd.generator;

import com.google.common.collect.Iterables;
import dk.sdu.mmmi.mdsd.math.Binding;
import dk.sdu.mmmi.mdsd.math.Div;
import dk.sdu.mmmi.mdsd.math.ExtCall;
import dk.sdu.mmmi.mdsd.math.LetBinding;
import dk.sdu.mmmi.mdsd.math.MathExp;
import dk.sdu.mmmi.mdsd.math.MathNumber;
import dk.sdu.mmmi.mdsd.math.Minus;
import dk.sdu.mmmi.mdsd.math.Mult;
import dk.sdu.mmmi.mdsd.math.Parenthesis;
import dk.sdu.mmmi.mdsd.math.Plus;
import dk.sdu.mmmi.mdsd.math.Program;
import dk.sdu.mmmi.mdsd.math.VarBinding;
import dk.sdu.mmmi.mdsd.math.VariableUse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.AbstractGenerator;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

/**
 * Generates code from your model files on save.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#code-generation
 */
@SuppressWarnings("all")
public class MathGenerator extends AbstractGenerator {
  private static Map<String, Integer> variables;
  
  @Override
  public void doGenerate(final Resource resource, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
    Iterable<Program> _filter = Iterables.<Program>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()), Program.class);
    for (final Program math : _filter) {
      String _name = math.getName();
      String _plus = ("math_expression/" + _name);
      String _plus_1 = (_plus + ".java");
      fsa.generateFile(_plus_1, this.generateProgram(math));
    }
  }
  
  public String generateProgram(final Program program) {
    final String classname = program.getName();
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package math_expression;");
    _builder.newLine();
    _builder.append("import java.util.*;");
    _builder.newLine();
    _builder.append("public class <<classname>> {");
    _builder.newLine();
    _builder.append("<<FOR variable : program.math.variables>>");
    _builder.newLine();
    _builder.append("public int <<variable.name>>;");
    _builder.newLine();
    _builder.append("<<ENDFOR>>");
    _builder.newLine();
    _builder.append("<<IF program.externals.size != 0>>private External external<<ENDIF>>");
    _builder.newLine();
    _builder.append("public <<classname>>(<<IF program.externals.size != 0>>External external<<ENDIF>>) {");
    _builder.newLine();
    _builder.append("<<IF program.externals.size != 0>>this.external = external;<<ENDIF>>");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("public void compute() {");
    _builder.newLine();
    _builder.append("<<FOR variable : program.math.variables>>");
    _builder.newLine();
    _builder.append("<<variable.name>> = <<variable.computeExpression()>>;");
    _builder.newLine();
    _builder.append("<<ENDFOR>>");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("public interface External{");
    _builder.newLine();
    _builder.append("<<FOR external : externals>>");
    _builder.newLine();
    _builder.append("public int <<external.name>>(<<FOR arguments : external.args SEPARATOR \',\' >><<arguments>> <<Character.toChars(characterCounter++).toString().substring(1,2)>><<ENDFOR>>);");
    _builder.newLine();
    _builder.append("<<ENDFOR>>");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  public static Map<String, Integer> compute(final MathExp math) {
    Map<String, Integer> _xblockexpression = null;
    {
      HashMap<String, Integer> _hashMap = new HashMap<String, Integer>();
      MathGenerator.variables = _hashMap;
      EList<VarBinding> _variables = math.getVariables();
      for (final VarBinding varBinding : _variables) {
        MathGenerator.computeExpression(varBinding);
      }
      _xblockexpression = MathGenerator.variables;
    }
    return _xblockexpression;
  }
  
  protected static CharSequence _computeExpression(final VarBinding binding) {
    CharSequence _xblockexpression = null;
    {
      System.out.println(binding.getName());
      _xblockexpression = MathGenerator.computeExpression(binding.getExpression());
    }
    return _xblockexpression;
  }
  
  protected static CharSequence _computeExpression(final MathNumber exp) {
    CharSequence _xblockexpression = null;
    {
      System.out.println(exp.getValue());
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<<exp.value>>");
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  protected static CharSequence _computeExpression(final Plus exp) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<<exp.left.computeExpression>> + <<exp.right.computeExpression>>");
    return _builder;
  }
  
  protected static CharSequence _computeExpression(final Minus exp) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<<exp.left.computeExpression>> - <<exp.right.computeExpression>>");
    return _builder;
  }
  
  protected static CharSequence _computeExpression(final Mult exp) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<<exp.left.computeExpression>> * <<exp.right.computeExpression>>");
    return _builder;
  }
  
  protected static CharSequence _computeExpression(final Div exp) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<<exp.left.computeExpression>> / <<exp.right.computeExpression>>");
    return _builder;
  }
  
  protected static CharSequence _computeExpression(final LetBinding exp) {
    return MathGenerator.computeExpression(exp.getBody());
  }
  
  protected static CharSequence _computeExpression(final VariableUse exp) {
    return MathGenerator.computeBinding(exp.getRef());
  }
  
  protected static CharSequence _computeBinding(final VarBinding binding) {
    CharSequence _xblockexpression = null;
    {
      boolean _containsKey = MathGenerator.variables.containsKey(binding.getName());
      boolean _not = (!_containsKey);
      if (_not) {
        MathGenerator.computeExpression(binding);
      }
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<<binding.name>>");
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  protected static String _computeBinding(final LetBinding binding) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(<<binding.binding.computeExpression>>)");
    return _builder.toString();
  }
  
  protected static CharSequence _computeExpression(final Parenthesis exp) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(<<exp.exp.computeExpression>>)");
    return _builder;
  }
  
  protected static CharSequence _computeExpression(final ExtCall exp) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("this.external.<<exp.ref.name>>(<<FOR expression:exp.args  SEPARATOR \',\' >><<expression.computeExpression>><<ENDFOR>>)");
    return _builder;
  }
  
  public static CharSequence computeExpression(final EObject exp) {
    if (exp instanceof Div) {
      return _computeExpression((Div)exp);
    } else if (exp instanceof ExtCall) {
      return _computeExpression((ExtCall)exp);
    } else if (exp instanceof LetBinding) {
      return _computeExpression((LetBinding)exp);
    } else if (exp instanceof MathNumber) {
      return _computeExpression((MathNumber)exp);
    } else if (exp instanceof Minus) {
      return _computeExpression((Minus)exp);
    } else if (exp instanceof Mult) {
      return _computeExpression((Mult)exp);
    } else if (exp instanceof Plus) {
      return _computeExpression((Plus)exp);
    } else if (exp instanceof VarBinding) {
      return _computeExpression((VarBinding)exp);
    } else if (exp instanceof VariableUse) {
      return _computeExpression((VariableUse)exp);
    } else if (exp instanceof Parenthesis) {
      return _computeExpression((Parenthesis)exp);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(exp).toString());
    }
  }
  
  public static CharSequence computeBinding(final Binding binding) {
    if (binding instanceof LetBinding) {
      return _computeBinding((LetBinding)binding);
    } else if (binding instanceof VarBinding) {
      return _computeBinding((VarBinding)binding);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(binding).toString());
    }
  }
}

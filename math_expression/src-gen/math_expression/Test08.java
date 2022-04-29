package math_expression;
import java.util.*;
public class <<classname>> {

<<getAttribute(program)>>

public <<classname>>(<<IF program.externals.size != 0>>External external<<ENDIF>>) {
<<IF program.externals.size != 0>>this.external = external;<<ENDIF>>
}

<<getComputeCharSequence(program.math)>>

<<getExternalInterface(program.externals)>>
}

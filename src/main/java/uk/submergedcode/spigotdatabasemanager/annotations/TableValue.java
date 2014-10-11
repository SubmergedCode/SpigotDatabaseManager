package uk.submergedcode.spigotdatabasemanager.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Sam Oates <sam@samoatesgames.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableValue {
    
    /**
     * The name of the table value
     * @return The name of the table value
     */
    String name();
    
}

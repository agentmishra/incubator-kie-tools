package org.jboss.errai.ui.shared.api.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;

/**
 * This annotation may only be used in subclasses of {@link Composite} that has been annotated with {@link Templated},
 * or in a super-class of said {@link Composite} types.
 * <p>
 * Indicate the types of events to be handled when used in conjunction with a corresponding method that has been
 * annotated with {@link EventHandler}; these types are specified using a bit mask in the {@link #value()} attribute.
 * <p>
 * <b>Native events on DOM Elements</b>
 * <p>
 * This use of this annotation does not require that target {@link Element} instances be referenced via
 * {@link DataField} in the {@link Templated} {@link Composite} component; they may also target un-referenced
 * <code>data-field</code> elements from the corresponding HTML template.
 * <p>
 * Example:
 * <p>
 * 
 * <pre>
 * &#064;Templated
 * public class QuickHandlerComponent extends Composite
 * {
 *    &#064;DataField
 *    private AnchorElement link = DOM.createAnchor().cast();
 * 
 *    &#064;EventHandler(&quot;link&quot;)
 *    &#064;SinkNative(Event.ONMOUSEOVER)
 *    public void doSomething(Event e)
 *    {
 *       // do something
 *    }
 * 
 *    &#064;EventHandler(&quot;div&quot;)
 *    &#064;SinkNative(Event.ONCLICK | Event.ONMOUSEOVER)
 *    public void doSomethingElse(Event e)
 *    {
 *       // do something with an element not otherwise referenced in our component
 *    }
 * }
 * </pre>
 * 
 * <p>
 * <b>See also:</b> {@link EventHandler}, {@link Templated}, {@link DataField}
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Inherited
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SinkNative
{
   /**
    * Specifies the types of {@link Event} instances that are handled by the annotated handler method. Multiple event
    * types may be handled using a bit-mask of the form:
    * <p>
    * 
    * <pre>
    * &#064;EventHandler(&quot;div&quot;)
    * &#064;SinkNative(Event.ONCLICK | Event.ONMOUSEOVER)
    * public void doSomething(Event e)
    * {
    *    // do something
    * }
    * </pre>
    */
   int value();
}

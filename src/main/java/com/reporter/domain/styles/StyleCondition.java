package com.reporter.domain.styles;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

/**
 * The class of the style applicability condition for the domain object,
 * used by StyleManager in logic: if condition predicate
 * is executed on the object and the class of the object is {@link StyleCondition#clazz},
 * then the style is transferred/applied to the object.
 */
public class StyleCondition {
    private static final Logger log = LoggerFactory.getLogger(StyleCondition.class);

    /**
     * Object styling condition
     */
    protected Predicate<?> predicate;
    /**
     * Object class
     */
    protected Class<?> clazz;

    public static StyleCondition create(Predicate<?> condition, Class<?> clazz) {
        return new StyleCondition().setPredicate(condition).setClazz(clazz);
    }

    /**
     * Checking the condition of object styling,
     * the check should not change the condition itself or other styles.
     *
     * @param t styling DocumentItem object
     * @param <T> style object type, T extends DocumentItem
     * @return true if the condition is not set, or it is true on the object; otherwise - false
     */
    @SuppressWarnings("unchecked")
    public <T> boolean test(T t) {
        log.trace("Check predicate:{} on {}", predicate, t);

        var result = true;

        if (predicate != null) {
            log.trace("Condition contains predicate:{}.", predicate);

            result = ((Predicate<T>) predicate).test(t);

            // Output is optional here. Left to demonstrate the approach.
            log.trace("Predicate check is:{}", result);
        } else {
            log.trace("No predicate found. Use true.");
        }

        // Here we output in trace, since StyleCondition is an inner class and there can be such conditions
        // many nested within each other.
        log.trace("Return check result:{}", result);

        return result;
    }

    public StyleCondition negate() {
        return StyleCondition.create(predicate.negate(), clazz);
    }

    public Predicate<?> getPredicate() {
        return predicate;
    }

    public StyleCondition setPredicate(Predicate<?> predicate) {
        this.predicate = predicate;
        return this;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public StyleCondition setClazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }

    @Override
    public String toString() {
        return
            MoreObjects.toStringHelper(this)
                .add("predicate", predicate)
                .add("clazz", clazz)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final StyleCondition that = (StyleCondition) o;

        return
            Objects.equal(this.predicate, that.predicate)
                && Objects.equal(this.clazz, that.clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(predicate, clazz);
    }
}

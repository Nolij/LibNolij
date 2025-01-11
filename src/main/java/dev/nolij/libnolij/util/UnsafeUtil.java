package dev.nolij.libnolij.util;

import dev.nolij.libnolij.refraction.Refraction;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

public final class UnsafeUtil {
    
    private UnsafeUtil() {}
    
    public static final Unsafe UNSAFE;
    public static final MethodHandles.Lookup INTERNAL_LOOKUP;
    public static final Refraction UNSAFE_REFRACTION;
    
    static {
        try {
            final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            UNSAFE = (Unsafe) unsafeField.get(null);
            
            final Field internalLookupField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
			//noinspection deprecation
			final long fieldOffset = UNSAFE.staticFieldOffset(internalLookupField);
			//noinspection deprecation
			final Object fieldBase = UNSAFE.staticFieldBase(internalLookupField);
            
            INTERNAL_LOOKUP = (MethodHandles.Lookup) UNSAFE.getObject(fieldBase, fieldOffset);
            UNSAFE_REFRACTION = new Refraction(INTERNAL_LOOKUP, Refraction.class.getClassLoader());
        } catch (Throwable e) {
            throw new AssertionError(e);
        }
    }
    
    public static void exit(int exitCode) {
        try {
            System.exit(exitCode);
        } catch (Throwable ignored) {
            Runtime.getRuntime().halt(exitCode);
        }
    }
    
}

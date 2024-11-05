package dev.nolij.libnolij.refraction;

import dev.nolij.libnolij.util.UnsafeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.util.Arrays;
import java.util.Objects;

public final class Refraction {
	
	private static final Refraction PUBLIC =
		new Refraction(MethodHandles.lookup());
	
	public static Refraction safe() {
		return PUBLIC;
	}
	
	public static Refraction unsafe() {
		return UnsafeUtil.UNSAFE_REFRACTION;
	}
	
	private final @NotNull MethodHandles.Lookup lookup;
	private final @NotNull ClassLoader classLoader;
	
	public Refraction(@NotNull final MethodHandles.Lookup lookup) {
		this(lookup, lookup.lookupClass().getClassLoader());
	}
	
	public Refraction(@NotNull final MethodHandles.Lookup lookup, @NotNull final ClassLoader classLoader) {
		this.lookup = lookup;
		this.classLoader = classLoader;
	}
	
	@SafeVarargs
	public static <T> @Nullable T firstNonNull(@Nullable T... options) {
		for (final T option : options)
			if (option != null)
				return option;
		
		return null;
	}
	
	public @Nullable Class<?> getClassOrNull(@NotNull final String className) {
		try {
			return Class.forName(className, false, classLoader);
		} catch (ClassNotFoundException ignored) {
			return null;
		}
	}
	
	public @Nullable Class<?> getClassOrNull(@NotNull final String... classNames) {
		for (final String className : classNames) {
			try {
				return Class.forName(className, false, classLoader);
			} catch (ClassNotFoundException ignored) { }
		}
		
		return null;
	}
	
	public @Nullable MethodHandle getMethodOrNull(@Nullable final Class<?> clazz, 
	                                              @NotNull final String methodName,
	                                              @Nullable final Class<?>... parameterTypes) {
		if (clazz == null || Arrays.stream(parameterTypes).anyMatch(Objects::isNull))
			return null;
		
		try {
			return lookup.unreflect(clazz.getMethod(methodName, parameterTypes));
		} catch (ReflectiveOperationException ignored) {
			return null;
		}
	}
	
	public @Nullable MethodHandle getMethodOrNull(@Nullable final Class<?> clazz, 
	                                              @NotNull final String methodName,
	                                              @Nullable final MethodType methodType,
	                                              @Nullable final Class<?>... parameterTypes) {
		if (clazz == null || Arrays.stream(parameterTypes).anyMatch(Objects::isNull))
			return null;
		
		try {
			return lookup.unreflect(clazz.getMethod(methodName, parameterTypes))
				.asType(methodType);
		} catch (ReflectiveOperationException ignored) {
			return null;
		}
	}
	
	public @Nullable MethodHandle getConstructorOrNull(@Nullable final Class<?> clazz,
	                                                   @NotNull final MethodType methodType,
	                                                   @Nullable final Class<?>... parameterTypes) {
		if (clazz == null || Arrays.stream(parameterTypes).anyMatch(Objects::isNull))
			return null;
		
		try {
			return lookup.unreflectConstructor(clazz.getConstructor(parameterTypes))
				.asType(methodType);
		} catch (ReflectiveOperationException ignored) {
			return null;
		}
	}
	
	public @Nullable MethodHandle getGetterOrNull(@Nullable final Class<?> clazz, 
												  @NotNull final String fieldName,
	                                              @Nullable final Class<?> fieldType) {
		if (clazz == null || fieldType == null)
			return null;
		
		try {
			return lookup.findGetter(clazz, fieldName, fieldType);
		} catch (ReflectiveOperationException ignored) {
			return null;
		}
	}
	
	public @Nullable MethodHandle getStaticGetterOrNull(@Nullable final Class<?> clazz, 
												  		@NotNull final String fieldName,
	                                              		@Nullable final Class<?> fieldType) {
		if (clazz == null || fieldType == null)
			return null;
		
		try {
			return lookup.findStaticGetter(clazz, fieldName, fieldType);
		} catch (ReflectiveOperationException ignored) {
			return null;
		}
	}
	
	public @Nullable MethodHandle getGetterOrNull(@Nullable final Class<?> clazz, 
												  @NotNull final String fieldName,
	                                              @Nullable final Class<?> fieldType, 
												  @NotNull final MethodType methodType) {
		if (clazz == null || fieldType == null)
			return null;
		
		try {
			return lookup.findGetter(clazz, fieldName, fieldType)
				.asType(methodType);
		} catch (ReflectiveOperationException ignored) {
			return null;
		}
	}
	
	public @Nullable MethodHandle getStaticGetterOrNull(@Nullable final Class<?> clazz, 
													  	@NotNull final String fieldName,
													  	@Nullable final Class<?> fieldType, 
													  	@NotNull final MethodType methodType) {
		if (clazz == null || fieldType == null)
			return null;
		
		try {
			return lookup.findStaticGetter(clazz, fieldName, fieldType)
				.asType(methodType);
		} catch (ReflectiveOperationException ignored) {
			return null;
		}
	}
	
	public @Nullable MethodHandle getSetterOrNull(@Nullable final Class<?> clazz, 
												  @NotNull final String fieldName,
	                                              @Nullable final Class<?> fieldType) {
		if (clazz == null || fieldType == null)
			return null;
		
		try {
			return lookup.findSetter(clazz, fieldName, fieldType);
		} catch (ReflectiveOperationException ignored) {
			return null;
		}
	}
	
	public @Nullable MethodHandle getStaticSetterOrNull(@Nullable final Class<?> clazz, 
												  		@NotNull final String fieldName,
	                                              		@Nullable final Class<?> fieldType) {
		if (clazz == null || fieldType == null)
			return null;
		
		try {
			return lookup.findStaticSetter(clazz, fieldName, fieldType);
		} catch (ReflectiveOperationException ignored) {
			return null;
		}
	}
	
	public @Nullable MethodHandle getSetterOrNull(@Nullable final Class<?> clazz, 
												  @NotNull final String fieldName,
	                                              @Nullable final Class<?> fieldType, 
												  @NotNull final MethodType methodType) {
		if (clazz == null || fieldType == null)
			return null;
		
		try {
			return lookup.findSetter(clazz, fieldName, fieldType)
				.asType(methodType);
		} catch (ReflectiveOperationException ignored) {
			return null;
		}
	}
	
	public @Nullable MethodHandle getStaticSetterOrNull(@Nullable final Class<?> clazz, 
												  		@NotNull final String fieldName,
	                                              		@Nullable final Class<?> fieldType, 
												  		@NotNull final MethodType methodType) {
		if (clazz == null || fieldType == null)
			return null;
		
		try {
			return lookup.findStaticSetter(clazz, fieldName, fieldType)
				.asType(methodType);
		} catch (ReflectiveOperationException ignored) {
			return null;
		}
	}
	
	public @Nullable VarHandle getFieldOrNull(@Nullable final Class<?> clazz, 
											  @NotNull final String fieldName,
											  @Nullable final Class<?> fieldType) {
		if (clazz == null || fieldType == null)
			return null;
		
		try {
			return lookup.findVarHandle(clazz, fieldName, fieldType)
				.withInvokeExactBehavior();
		} catch (NoSuchFieldException | IllegalAccessException ignored) {
			return null;
		}
	}
	
	public @Nullable VarHandle getStaticFieldOrNull(@Nullable final Class<?> clazz, 
													@NotNull final String fieldName, 
													@Nullable final Class<?> fieldType) {
		if (clazz == null || fieldType == null)
			return null;
		
		try {
			return lookup.findStaticVarHandle(clazz, fieldName, fieldType)
				.withInvokeExactBehavior();
		} catch (NoSuchFieldException | IllegalAccessException ignored) {
			return null;
		}
	}
	
}

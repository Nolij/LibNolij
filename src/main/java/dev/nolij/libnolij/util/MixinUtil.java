package dev.nolij.libnolij.util;

import dev.nolij.libnolij.refraction.Refraction;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.service.MixinService;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.List;

public final class MixinUtil {
	
	private MixinUtil() {}
	
	private static final Class<?> MIXIN_TRANSFORMER_CLASS = 
		Refraction.unsafe().getClassOrNull("org.spongepowered.asm.mixin.transformer.MixinTransformer");
	private static final Class<?> MIXIN_PROCESSOR_CLASS = 
		Refraction.unsafe().getClassOrNull("org.spongepowered.asm.mixin.transformer.MixinProcessor");
	
	private static final MethodHandle GET_PROCESSOR = Refraction.unsafe().getGetterOrNull(
		MIXIN_TRANSFORMER_CLASS,
		"processor",
		MIXIN_PROCESSOR_CLASS,
		MethodType.methodType(Object.class, IMixinTransformer.class)
	);
	
	private static final MethodHandle GET_CONFIGS = Refraction.unsafe().getGetterOrNull(
		MIXIN_PROCESSOR_CLASS,
		"configs",
		List.class,
		MethodType.methodType(List.class, Object.class)
	);
	
	public static void audit() throws Throwable {
		var env = MixinEnvironment.getCurrentEnvironment();
		if (env.getActiveTransformer() instanceof IMixinTransformer transformer) {
			final Object processor;
			final List<IMixinConfig> configs;
			//noinspection DataFlowIssue
			processor = GET_PROCESSOR.invokeExact(transformer);
			//noinspection unchecked,DataFlowIssue
			configs = (List<IMixinConfig>) GET_CONFIGS.invokeExact(processor);
			
			var provider = MixinService.getService().getClassProvider();
			for (var config : configs) {
				for (String target : config.getTargets()) {
					try {
						provider.findClass(target, false);
					} catch (Throwable t) {
						System.err.println("Failed to load " + target);
						throw t;
					}
				}
			}
		}
	}
	
	@SuppressWarnings("CallToPrintStackTrace")
	public static void auditAndExit() {
		var exitCode = 0;
		try {
			audit();
		} catch (Throwable t) {
			t.printStackTrace();
			exitCode = 1;
		}
		
		UnsafeUtil.exit(exitCode);
	}
	
}

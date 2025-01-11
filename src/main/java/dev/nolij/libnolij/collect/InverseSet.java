package dev.nolij.libnolij.collect;

import java.util.Set;
import java.util.stream.Collectors;

public class InverseSet<T> {
	
	private final Set<T> delegate;
	
	@SafeVarargs
	public static <E> InverseSet<E> of(E... elements) {
		return new InverseSet<>(Set.of(elements));
	}
	
	public InverseSet(Set<T> delegate) {
		this.delegate = delegate.stream().collect(Collectors.toUnmodifiableSet());
	}
	
	public boolean contains(T t) {
		return !delegate.contains(t);
	}
	
}

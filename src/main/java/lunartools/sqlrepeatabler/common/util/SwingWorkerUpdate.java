package lunartools.sqlrepeatabler.common.util;

public class SwingWorkerUpdate<T> {
	public final Enum<?> step;
	public final T data;

	public SwingWorkerUpdate(Enum<?> step, T data) {
		this.step = step;
		this.data = data;
	}
}

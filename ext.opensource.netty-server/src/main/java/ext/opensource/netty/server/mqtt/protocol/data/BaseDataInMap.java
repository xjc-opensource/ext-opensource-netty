package ext.opensource.netty.server.mqtt.protocol.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class BaseDataInMap<K, V> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private ConcurrentHashMap<K, V> map= new ConcurrentHashMap<K, V>();

	public BaseDataInMap() {
	}
	public BaseDataInMap(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void put(K key, V value) {
		map.put(key, value);
	}

	public V get(K key) {
		return map.get(key);
	}

	public boolean containsKey(K key) {
		return map.containsKey(key);
	}

	public void remove(K key) {
		map.remove(key);
	}

	public long size() {
		return map.size();
	}

	public Collection<V> values() {
		return map.values();
	}
	
	public Collection<K> keys() {
		return map.keySet();
	}

	public Set<Map.Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	public void clear() {
		map.clear();
	}

	public void forEach(BiConsumer<? super K, ? super V> action) {
		map.forEach(action);
	}
}

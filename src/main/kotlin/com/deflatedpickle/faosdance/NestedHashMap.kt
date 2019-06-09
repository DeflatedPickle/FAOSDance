package com.deflatedpickle.faosdance

class NestedHashMap<K, V> : HashMap<K, V>() {
    fun getMap(key: K): NestedHashMap<K, V>? {
        return get(key) as NestedHashMap<K, V>
    }

    fun <IK> getOption(key: K): IK? {
        return get(key) as IK
    }

    fun setOption(key: K, value: V) {
        this[key] = value
    }
}
package com.omricat.countdown.model


internal interface BiMap<E : Any, F : Any> : Map<E, F> {
  override val values: Set<F>
  fun inverseBiMap(): BiMap<F, E>
}

internal fun <E : Any, F : Any> Map<E, F>.toBiMap(): BiMap<E, F> = ImmutableBiMap(this)


internal class ImmutableBiMap<E : Any, F : Any>
private constructor(
  private val directMap: Map<E, F>,
  private val inverseMap: Map<F, E>
) : BiMap<E, F> {

  constructor(map: Map<E, F>) : this(
    directMap = map,
    inverseMap = map.entries.associate { (e, f) -> f to e }
  )

  init {
    require(inverseMap.keys.containsAll(directMap.values))
    require(directMap.keys.containsAll(inverseMap.values))
  }

  override val values: Set<F> get() = inverseMap.keys

  override fun inverseBiMap(): BiMap<F, E> =
    ImmutableBiMap(directMap = inverseMap, inverseMap = directMap)

  override val entries: Set<Map.Entry<E, F>> get() = directMap.entries

  override val keys: Set<E>
    get() = directMap.keys
  override val size: Int
    get() = directMap.size

  override fun isEmpty(): Boolean = directMap.isEmpty()

  override fun get(key: E): F? = directMap[key]

  override fun containsValue(value: F): Boolean = inverseMap.containsKey(value)

  override fun containsKey(key: E): Boolean = directMap.containsKey(key)

}

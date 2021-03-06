package skinny.orm.feature.associations

import scala.language.existentials

import skinny.orm._
import skinny.orm.feature._

/**
  * Extractor.
  *
  * @tparam Entity entity
  */
sealed trait Extractor[Entity]

/**
  * BelongsTo association extractor.
  *
  * @param mapper mapper
  * @param fk foreign key
  * @param alias table alias
  * @param merge function to merge associations
  * @param byDefault enable by default
  * @tparam Entity entity
  */
case class BelongsToExtractor[Entity](
    mapper: AssociationsFeature[_],
    fk: String,
    alias: Alias[_],
    merge: (Entity, Option[Any]) => Entity,
    includesMerge: (Seq[Entity], Seq[_]) => Seq[Entity] = AssociationsFeature.defaultIncludesMerge[Entity, Any],
    var byDefault: Boolean = false
) extends Extractor[Entity]

/**
  * HasOne association extractor.
  *
  * @param mapper mapper
  * @param fk foreign key
  * @param alias table alias
  * @param merge function to merge associations
  * @param byDefault enable by default
  * @tparam Entity entity
  */
case class HasOneExtractor[Entity](
    mapper: AssociationsFeature[_],
    fk: String,
    alias: Alias[_],
    merge: (Entity, Option[Any]) => Entity,
    includesMerge: (Seq[Entity], Seq[_]) => Seq[Entity] = AssociationsFeature.defaultIncludesMerge[Entity, Any],
    var byDefault: Boolean = false
) extends Extractor[Entity]

/**
  * HasMany association extractor.
  *
  * @param mapper mapper
  * @param fk foreign key
  * @param alias table alias
  * @param merge function to merge associations
  * @param byDefault enable by default
  * @tparam Entity entity
  */
case class HasManyExtractor[Entity](
    mapper: AssociationsFeature[_],
    fk: String,
    alias: Alias[_],
    merge: (Entity, Seq[Any]) => Entity,
    includesMerge: (Seq[Entity], Seq[_]) => Seq[Entity] = AssociationsFeature.defaultIncludesMerge[Entity, Any],
    var byDefault: Boolean = false
) extends Extractor[Entity]

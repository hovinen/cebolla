/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.repositories;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.cebolla.entities.EntityWithKey;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

/**
 * @author Bradford Hovinen <hovinen@gmail.com>
 */
public class AbstractFakeRepositoryWithSimpleLookup<Key, EntityType extends EntityWithKey<Key>>
	extends AbstractFakeRepository<EntityType> {
    private final Set<Key> keysLookedUp = new HashSet<>();

    private class KeyMatchPredicate implements Predicate<EntityType> {
	private final Key key;

	private KeyMatchPredicate(Key key) {
	    this.key = key;
	}

	@Override
	public boolean apply(EntityType input) {
	    return Objects.equal(key, input.getKey());
	}
    }

    public Optional<EntityType> lookup(Key key) {
	keysLookedUp.add(key);

	for (EntityType entity : filter(new KeyMatchPredicate(key))) {
	    return Optional.of(entity);
	}

	return Optional.absent();
    }

    @SuppressWarnings("unchecked")
    public void assertKeyLookedUp(Key key) {
	assertThat(keysLookedUp, contains(key));
    }

    public void clearLookupRecords() {
	keysLookedUp.clear();
    }
}

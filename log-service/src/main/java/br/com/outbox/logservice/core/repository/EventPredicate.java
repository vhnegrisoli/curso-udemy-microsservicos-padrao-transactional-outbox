package br.com.outbox.logservice.core.repository;

import br.com.outbox.logservice.core.dto.LogFilters;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import static java.lang.String.format;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
@AllArgsConstructor
public class EventPredicate {

    private static final String OPTIONS = "i";
    private static final String WILDCARD_REGEX = ".*%s.*";

    public Query definePredicate(LogFilters filters) {
        var criteria = new Criteria();

        wildcardFilter(criteria, "user.username", filters.username());
        wildcardFilter(criteria, "user.fullName", filters.fullName());
        wildcardFilter(criteria, "user.document", filters.document());
        wildcardFilter(criteria, "user.email", filters.email());
        wildcardFilter(criteria, "url", filters.url());
        equalsFilter(criteria, "user.status", filters.status());
        equalsFilter(criteria, "action", filters.action());
        equalsFilter(criteria, "httpMethod", filters.httpMethod());

        return new Query(criteria);
    }

    private Criteria equalsFilter(Criteria criteria, String field, Object value) {
        if (isFieldAndValueInformed(field, value)) {
            criteria.and(field).is(value);
        }
        return criteria;
    }

    private Criteria wildcardFilter(Criteria criteria, String field, Object value) {
        if (isFieldAndValueInformed(field, value)) {
            criteria.and(field).regex(format(WILDCARD_REGEX, value), OPTIONS);
        }
        return criteria;
    }

    private boolean isFieldAndValueInformed(String field, Object value) {
        return !isEmpty(field) && !isEmpty(value);
    }
}

package io.feedoong.api.domain.channel;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.feedoong.api.domain.channel.dto.ChannelDetailsDTO;
import io.feedoong.api.domain.dto.ChannelItemDTO;
import io.feedoong.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.Optional;

import static com.querydsl.core.types.Projections.constructor;
import static com.querydsl.core.types.dsl.Expressions.asBoolean;
import static io.feedoong.api.domain.channel.QChannel.channel;
import static io.feedoong.api.domain.subscription.QSubscription.subscription;

@RequiredArgsConstructor
public class ChannelRepositoryImpl implements CustomChannelRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ChannelDetailsDTO> getChannelDetails(Optional<User> user, Long channelId) {
        ChannelDetailsDTO subscribedChannelDTO = queryFactory
                .select(constructor(ChannelDetailsDTO.class,
                        channel.id,
                        channel.title,
                        channel.description,
                        channel.url,
                        channel.feedUrl,
                        channel.imageUrl,
                        asBoolean(isSubscribedOptExp(user)).as("isSubscribed")))
                .from(channel)
                .where(channel.id.eq(channelId))
                .fetchOne();

        return Optional.ofNullable(subscribedChannelDTO);
    }

    private BooleanExpression isSubscribedOptExp(Optional<User> user) {
        if (user.isPresent()) {
            return JPAExpressions
                    .selectFrom(subscription)
                    .where(subscription.channel.eq(channel).and(subscription.user.eq(user.get())))
                    .exists();
        } else {
            return Expressions.FALSE;
        }
    }
}

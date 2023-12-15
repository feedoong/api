package io.feedoong.api.domain.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.domain.dto.ChannelItemDTO;
import io.feedoong.api.domain.like.QLike;
import io.feedoong.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.Projections.constructor;
import static com.querydsl.core.types.dsl.Expressions.asBoolean;
import static io.feedoong.api.domain.channel.QChannel.channel;
import static io.feedoong.api.domain.item.QItem.item;
import static io.feedoong.api.domain.like.QLike.like;
import static io.feedoong.api.domain.subscription.QSubscription.subscription;
import static io.feedoong.api.domain.view.QView.view;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements CustomItemRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChannelItemDTO> findChannelItemByUser(Pageable pageable, User user) {
        List<ChannelItemDTO> contents = queryFactory
                .select(constructor(ChannelItemDTO.class,
                        item.id,
                        item.title,
                        item.description,
                        item.link,
                        item.guid,
                        item.publishedAt,
                        item.imageUrl,
                        asBoolean(isLikedExp(user)).as("isLiked"),
                        asBoolean(isViewedExp(user)).as("isViewed"),
                        channel.id,
                        channel.title,
                        channel.imageUrl))
                .from(item)
                .join(item.channel, channel)
                .join(subscription).on(subscription.channel.eq(channel).and(subscription.user.eq(user)))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
        JPAQuery<Item> countQuery = queryFactory
                .selectFrom(item)
                .join(item.channel, channel)
                .join(subscription).on(subscription.channel.eq(channel).and(subscription.user.eq(user)));

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchCount);
    }

    @Override
    public Page<ChannelItemDTO> findChannelItemsByChannel(Pageable pageable, Optional<User> user, Channel channelEntity) {
        List<ChannelItemDTO> contents = queryFactory
                .select(constructor(ChannelItemDTO.class,
                        item.id,
                        item.title,
                        item.description,
                        item.link,
                        item.guid,
                        item.publishedAt,
                        item.imageUrl,
                        asBoolean(isLikedOptExp(user)).as("isLiked"),
                        asBoolean(isViewedOptExp(user)).as("isViewed"),
                        channel.id,
                        channel.title,
                        channel.imageUrl))
                .from(item)
                .join(item.channel, channel).on(channel.eq(channelEntity))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
        JPAQuery<Item> countQuery = queryFactory
                .selectFrom(item)
                .join(item.channel, channel).on(channel.eq(channelEntity));

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchCount);
    }

    @Override
    public Page<ChannelItemDTO> findLikedChannelItems(Pageable pageable, User user) {
        List<ChannelItemDTO> contents = queryFactory
                .select(constructor(ChannelItemDTO.class,
                        item.id,
                        item.title,
                        item.description,
                        item.link,
                        item.guid,
                        item.publishedAt,
                        item.imageUrl,
                        asBoolean(true).as("isLiked"),
                        asBoolean(isViewedExp(user)).as("isViewed"),
                        channel.id,
                        channel.title,
                        channel.imageUrl))
                .from(item)
                .join(item.channel, channel)
                .join(like).on(like.user.eq(user).and(like.item.eq(item)))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
        JPAQuery<Item> countQuery = queryFactory
                .selectFrom(item)
                .join(item.channel, channel);

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchCount);
    }

    private BooleanExpression isLikedExp(User user) {
        return JPAExpressions
                .selectFrom(like)
                .where(like.user.eq(user), like.item.eq(item))
                .exists();
    }

    private BooleanExpression isLikedOptExp(Optional<User> user) {
        if (user.isPresent()) {
            return JPAExpressions
                    .selectFrom(like)
                    .where(like.user.eq(user.get()), like.item.eq(item))
                    .exists();
        } else {
            return Expressions.FALSE;
        }
    }

    private BooleanExpression isViewedExp(User user) {
        return JPAExpressions
                .selectFrom(view)
                .where(view.user.eq(user), view.item.eq(item))
                .exists();
    }

    private BooleanExpression isViewedOptExp(Optional<User> user) {
        if (user.isPresent()) {
            return JPAExpressions
                    .selectFrom(view)
                    .where(view.user.eq(user.get()), view.item.eq(item))
                    .exists();
        } else {
            return Expressions.FALSE;
        }
    }
}

package io.feedoong.api.domain.item;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.feedoong.api.domain.User;
import io.feedoong.api.domain.dto.ChannelItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static io.feedoong.api.domain.QChannel.channel;
import static io.feedoong.api.domain.QSubscription.subscription;
import static io.feedoong.api.domain.QView.view;
import static io.feedoong.api.domain.item.QItem.item;
import static io.feedoong.api.domain.like.QLike.like;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements CustomItemRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChannelItemDTO> findChannelItemByUser(Pageable pageable, User user) {
        BooleanExpression isLiked = JPAExpressions
                .selectFrom(like)
                .where(like.user.eq(user), like.item.eq(item))
                .exists();
        BooleanExpression isViewed = JPAExpressions
                .selectFrom(view)
                .where(view.user.eq(user), view.item.eq(item))
                .exists();

        List<ChannelItemDTO> contents = queryFactory
                .select(Projections.constructor(ChannelItemDTO.class,
                        item.id,
                        item.title,
                        item.description,
                        item.link,
                        item.guid,
                        item.publishedAt,
                        item.imageUrl,
                        Expressions.asBoolean(isLiked).as("isLiked"),
                        Expressions.asBoolean(isViewed).as("isViewed"),
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
                .join(subscription.channel, channel)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchCount);
    }
}

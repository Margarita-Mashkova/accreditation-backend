package ru.ulstu.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.ulstu.dto.RuleDto;
import ru.ulstu.model.Rule;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RuleMapper {
    Rule fromRuleDto(RuleDto ruleDto);
    RuleDto toRuleDto(Rule rule);
    @AfterMapping
    default void setIndicatorKey(Rule rule, @MappingTarget RuleDto ruleDto){
        ruleDto.setIndicatorKey(rule.getIndicator().getKey());
    }

}

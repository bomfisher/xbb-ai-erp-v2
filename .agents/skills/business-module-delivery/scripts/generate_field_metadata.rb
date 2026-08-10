#!/usr/bin/env ruby
# frozen_string_literal: true

require "json"
require "yaml"

FILTERABLE_FIELD_TYPES = %w[TEXT USER DEPT BUSINESS COMB COMB_MULTI CHECKBOX RADIO_BTN SWITCH NUM_INT NUM_DOUBLE AMOUNT STOCK DATE TIME].freeze
NON_FILTERABLE_FIELD_TYPES = %w[FILE IMAGE ADDRESS SUB_ITEM PRODUCT].freeze
REQUIRED_FIELD_KEYS = %w[name attr attrName fieldType scenes required editable defaultValue filterName].freeze
OPTION_FIELD_TYPES = %w[COMB COMB_MULTI CHECKBOX RADIO_BTN].freeze
FILTER_PROTOCOL_TYPES = {
  "TEXT" => "TEXT", "USER" => "ID", "DEPT" => "ID", "BUSINESS" => "BUSINESS",
  "COMB" => "ENUM", "RADIO_BTN" => "ENUM", "SWITCH" => "ENUM",
  "COMB_MULTI" => "ENUM_MULTI", "CHECKBOX" => "ENUM_MULTI",
  "NUM_INT" => "NUM_INT", "NUM_DOUBLE" => "NUM_DOUBLE", "AMOUNT" => "AMOUNT", "STOCK" => "STOCK",
  "DATE" => "DATE", "TIME" => "TIME"
}.freeze
FILTER_SUPPORTED_SYMBOLS = {
  "TEXT" => %w[EQ NE CONTAINS NOT_CONTAINS IS_EMPTY IS_NOT_EMPTY],
  "USER" => %w[EQ NE IN IS_EMPTY IS_NOT_EMPTY], "DEPT" => %w[EQ NE IN IS_EMPTY IS_NOT_EMPTY],
  "BUSINESS" => %w[EQ NE IN IS_EMPTY IS_NOT_EMPTY],
  "COMB" => %w[CONTAINS NOT_CONTAINS IS_EMPTY IS_NOT_EMPTY],
  "RADIO_BTN" => %w[CONTAINS NOT_CONTAINS IS_EMPTY IS_NOT_EMPTY],
  "SWITCH" => %w[CONTAINS NOT_CONTAINS IS_EMPTY IS_NOT_EMPTY],
  "COMB_MULTI" => %w[CONTAINS NOT_CONTAINS CONTAINS_ALL NOT_CONTAINS_ALL IS_EMPTY IS_NOT_EMPTY],
  "CHECKBOX" => %w[CONTAINS NOT_CONTAINS CONTAINS_ALL NOT_CONTAINS_ALL IS_EMPTY IS_NOT_EMPTY],
  "NUM_INT" => %w[EQ NE GE LE BETWEEN IS_EMPTY IS_NOT_EMPTY],
  "NUM_DOUBLE" => %w[EQ NE GE LE BETWEEN IS_EMPTY IS_NOT_EMPTY],
  "AMOUNT" => %w[EQ NE GE LE BETWEEN IS_EMPTY IS_NOT_EMPTY],
  "STOCK" => %w[EQ NE GE LE BETWEEN IS_EMPTY IS_NOT_EMPTY],
  "DATE" => %w[EQ GE LE BETWEEN IS_EMPTY IS_NOT_EMPTY],
  "TIME" => %w[GE LE BETWEEN IS_EMPTY IS_NOT_EMPTY]
}.freeze

def fail_with(message)
  warn message
  exit 1
end

input_path, output_path = ARGV
fail_with("用法：generate_field_metadata.rb <字段设计.yaml> <field-metadata.json>") unless input_path && output_path && ARGV.length == 2

design = YAML.safe_load(File.read(input_path, encoding: "UTF-8"), permitted_classes: [], aliases: false)
fail_with("字段设计根节点必须是对象") unless design.is_a?(Hash)
business_code = design["businessCode"]
fail_with("缺少 businessCode") unless business_code.is_a?(String) && business_code.match?(/\A[A-Z][A-Z0-9_]*\z/)
fields = design["fields"]
fail_with("fields 必须是数组") unless fields.is_a?(Array)

def normalize_field(field, path, child: false)
  fail_with("#{path} 必须是对象") unless field.is_a?(Hash)
  missing = REQUIRED_FIELD_KEYS.reject { |key| field.key?(key) }
  fail_with("#{path} 缺少：#{missing.join('、')}") unless missing.empty?
  field_type = field["fieldType"]
  filter_name = field["filterName"]
  fail_with("#{path} 是子档字段，filterName 必须为 null") if child && !filter_name.nil?
  if NON_FILTERABLE_FIELD_TYPES.include?(field_type) && !filter_name.nil?
    fail_with("#{path} 的 #{field_type} 不支持筛选，filterName 必须为 null")
  end
  if !filter_name.nil? && !FILTERABLE_FIELD_TYPES.include?(field_type)
    fail_with("#{path} 的 fieldType #{field_type} 未定义筛选规则，filterName 必须为 null")
  end
  normalized_field = field.slice(*REQUIRED_FIELD_KEYS)
  unless filter_name.nil?
    normalized_field["filterFieldType"] = FILTER_PROTOCOL_TYPES.fetch(field_type)
    normalized_field["supportedSymbols"] = FILTER_SUPPORTED_SYMBOLS.fetch(field_type)
  end
  if OPTION_FIELD_TYPES.include?(field_type) && field.key?("options")
    fail_with("#{path}.options 必须是非空字符串") unless field["options"].is_a?(String) && !field["options"].strip.empty?
    normalized_field["options"] = field["options"]
  end
  if field_type == "BUSINESS" && field.key?("businessCode")
    upstream_business_code = field["businessCode"]
    fail_with("#{path}.businessCode 必须是显式的大写枚举值") unless upstream_business_code.is_a?(String) && upstream_business_code.match?(/\A[A-Z][A-Z0-9_]*\z/)
    normalized_field["businessCode"] = upstream_business_code
  end
  if field_type == "SUB_ITEM"
    sub_fields = field["subFields"]
    fail_with("#{path}.subFields 必须是数组") unless sub_fields.is_a?(Array)
    normalized_field["subFields"] = sub_fields.map.with_index do |sub_field, index|
      normalize_field(sub_field, "#{path}.subFields[#{index}]", child: true)
    end
  end
  normalized_field
end

normalized_fields = fields.map.with_index do |field, index|
  normalize_field(field, "fields[#{index}]")
end

actions = design["listActions"]
fail_with("缺少 listActions") unless actions.is_a?(Hash)
%w[top bottom row].each { |group| fail_with("缺少 listActions.#{group}") unless actions[group].is_a?(Array) }

metadata = {
  "businessCode" => business_code,
  "fields" => normalized_fields,
  "listActions" => actions.slice("top", "bottom", "row")
}
File.write(output_path, JSON.pretty_generate(metadata) + "\n")
puts "字段元数据已生成：#{output_path}"

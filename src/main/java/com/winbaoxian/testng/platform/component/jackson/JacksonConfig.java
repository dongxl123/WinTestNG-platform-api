package com.winbaoxian.testng.platform.component.jackson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.winbaoxian.testng.enums.RequestContentType;
import com.winbaoxian.testng.model.core.action.ActionSetting;
import com.winbaoxian.testng.utils.ConfigParseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class JacksonConfig {

    @Autowired
    private MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;

    @PostConstruct
    public ObjectMapper configure() {
        ObjectMapper objectMapper = jackson2HttpMessageConverter.getObjectMapper();
        SimpleModule actionSettingModule = new SimpleModule();
        actionSettingModule.addDeserializer(ActionSetting.class, new ActionSettingDeserializer());
        actionSettingModule.addSerializer(RequestContentType.class, new RequestContentTypeSerializer());
        objectMapper.registerModule(actionSettingModule);
        return objectMapper;
    }

    public class ActionSettingDeserializer extends JsonDeserializer<ActionSetting> {

        @Override
        public ActionSetting deserialize(JsonParser jp, DeserializationContext context) throws IOException, JsonProcessingException {
            TreeNode rootNode = jp.readValueAsTree();
            if (rootNode == null || !rootNode.isObject()) {
                return null;
            }
            TreeNode actionTypeNode = rootNode.get("actionType");
            if (actionTypeNode == null) {
                return null;
            }
            return ConfigParseUtils.INSTANCE.parseOneActionSetting(JSON.parseObject(rootNode.toString(), Feature.OrderedField));
        }

        @Override
        public Class<ActionSetting> handledType() {
            return ActionSetting.class;
        }

    }

    public class RequestContentTypeSerializer extends JsonSerializer<RequestContentType> {

        @Override
        public void serialize(RequestContentType value, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {
            gen.writeString(value.getName());
        }

        @Override
        public Class<RequestContentType> handledType() {
            return RequestContentType.class;
        }
    }

}

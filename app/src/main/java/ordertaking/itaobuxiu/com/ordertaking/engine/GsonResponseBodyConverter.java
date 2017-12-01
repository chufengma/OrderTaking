package ordertaking.itaobuxiu.com.ordertaking.engine;

/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import ordertaking.itaobuxiu.com.ordertaking.apis.Response;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            String data = value.string();
            try {
                return adapter.fromJson(data);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Type type = new TypeToken<Response<JsonElement>>() {}.getType();
                    return gson.fromJson(data, type);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    throw e2;
                }
            }
        } finally {
            value.close();
        }
    }
}

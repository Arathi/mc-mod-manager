package com.undsf.mcmodmgr.curseforge;

import com.fasterxml.jackson.core.type.TypeReference;
import com.undsf.mcmodmgr.curseforge.enums.ModLoaderType;
import com.undsf.mcmodmgr.curseforge.enums.ModsSearchSortField;
import com.undsf.mcmodmgr.curseforge.models.Mod;
import com.undsf.mcmodmgr.curseforge.models.ModFile;
import com.undsf.mcmodmgr.curseforge.models.PaginationResponse;
import com.undsf.mcmodmgr.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CurseForgeApi {
    public static final int PAGE_SIZE = 50;
    public static final String BASE_URL = "https://api.curseforge.com";
    public static final String API_KEY_HEADER_NAME = "x-api-key";
    public static final int GameIdMinecraft = 432;

    public OkHttpClient httpClient;
    public String apiKey;

    public CurseForgeApi(OkHttpClient httpClient, String apiKey) {
        this.httpClient = httpClient;
        this.apiKey = apiKey;
    }

    // region 创建报文体
    public String createQuery(FormBody form) {
        StringBuilder query = new StringBuilder();
        for (int i = 0; i < form.size(); i++) {
            query.append(form.encodedName(i));
            query.append("=");
            query.append(form.encodedValue(i));
        }
        return query.toString();
    }

    public String createQuery(Map<String, String> params) {
        FormBody form = createFormBody(params);
        return createQuery(form);
    }

    public FormBody createFormBody(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }
    // endregion

    // region GET请求
    public String get(String uri, Map<String, String> params) {
        StringBuilder url = new StringBuilder();
        url.append(BASE_URL);
        url.append(uri);

        if (params != null && params.size() > 0) {
            url.append("?");
            url.append(createQuery(params));
        }

        Request req = new Request.Builder()
                .header(API_KEY_HEADER_NAME, apiKey)
                .get()
                .url(url.toString())
                .build();

        try {
            Response resp = httpClient.newCall(req).execute();
            return resp.body().string();
        }
        catch (IOException ex) {
            log.warn("发送GET请求发生异常：{}", url, ex);
        }
        return null;
    }

    public <T> T get(String uri, Map<String, String> params, Class<T> type) {
        String json = get(uri, params);
        return JsonUtil.parse(json, type);
    }

    public <T> T get(String uri, Map<String, String> params, TypeReference<T> type) {
        String json = get(uri, params);
        return JsonUtil.parse(json, type);
    }
    // endregion

    // region POST请求
    public String post(String uri, Map<String, String> params) {
        String url = BASE_URL + uri;
        FormBody form = createFormBody(params);
        Request req = new Request.Builder()
                .header(API_KEY_HEADER_NAME, apiKey)
                .post(form)
                .url(url)
                .build();

        try {
            Response resp = httpClient.newCall(req).execute();
            return resp.body().string();
        }
        catch (IOException ex) {
            log.warn("发送POST请求发生异常：{} {}", url, createQuery(form), ex);
        }
        return null;
    }

    public <T> T post(String uri, Map<String, String> form, Class<T> type) {
        String json = post(uri, form);
        return JsonUtil.parse(json, type);
    }

    public <T> T post(String uri, Map<String, String> form, TypeReference<T> type) {
        String json = post(uri, form);
        return JsonUtil.parse(json, type);
    }
    // endregion

    // region MOD
    public List<Mod> search(
            Integer classId,
            Integer categoryId,
            String gameVersion,
            String searchFilter,
            ModsSearchSortField sortField,
            String sortOrder,
            ModLoaderType modLoaderType,
            Integer gameVersionTypeId,
            String slug,
            Integer index,
            Integer pageSize) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("gameId", GameIdMinecraft + "");

        TypeReference<PaginationResponse<Mod>> type = new TypeReference<>() {};
        PaginationResponse<Mod> mods = get("/v1/mods/search", params, type);
        return mods.data;
    }

    public Mod getMod(int modId) {
        TypeReference<PaginationResponse<Mod>> type = new TypeReference<>() {};
        return null;
    }
    // endregion

    // region MOD File
    public ModFile getModFile(int modId, int fileId) {
        return null;
    }

    public List<ModFile> getModFiles(
            int modId,
            String gameVersion,
            ModLoaderType modLoaderType,
            Integer gameVersionTypeId,
            Integer index,
            Integer pageSize) {
        return null;
    }
    // endregion
}

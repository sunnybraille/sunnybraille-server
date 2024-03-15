package sunflower.server.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import sunflower.server.application.dto.BrfFileDto;

@Getter
public class BrfFileQueryResponse {

    @Schema(description = "Translations ID", defaultValue = "1")
    private final Long id;

    @Schema(description = "점역 결과 이름 (기본값은 original file name과 동일)", defaultValue = "28번.pdf")
    private final String name;

    @Schema(description = "Original File Name", defaultValue = "28번.pdf")
    private final String originalFileName;

    @Schema(description = "brf 파일 내용",
            defaultValue = "  \\documentclass82#ajpt;081article\"0\n\\usepackage82utf#h;081inputenc\"0\n\\usepackage82T#a;081fontenc\"0\n\\usepackage81CJKutf#h\"0\n\\usepackage81amsmath\"0\n\\usepackage81amsfonts\"0\n\\usepackage81amssymb\"0\n\\usepackage82version=#d;081mhchem\"0\n\\usepackage81stmaryrd\"0\n\n\\begin81document\"0\n\\begin81CJK\"081UTF#h\"081mj\"0\n\\begin81enumerate\"0\n  \\setcounter81enumi\"081#bg\"0\n  \\item $p<q$ q im ,u,m $p\" q$ n irj<: $p^81#b\"0 q<n \\leq p q^81#b\"0$ ! e3.x,ofocz .<*,m $n$ w @r,m$ #cjh o1 ,ir\" $p+q$ w $b'! @mj,ou4 82#d.s5;0\n\\end81enumerate\"0\n\n\\end81CJK\"0\n\\end81document\"0â ")
    private final String content;

    public BrfFileQueryResponse(
            final Long id,
            final String name,
            final String originalFileName,
            final String content
    ) {
        this.id = id;
        this.name = name;
        this.originalFileName = originalFileName;
        this.content = content;
    }

    public static BrfFileQueryResponse from(final Long id, final BrfFileDto brfFile) {
        return new BrfFileQueryResponse(id, brfFile.getName(), brfFile.getOriginalFileName(), brfFile.getContent());
    }
}

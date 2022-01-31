package sn.coundoul.gestion.utilisateurs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.utilisateurs.web.rest.TestUtil;

class MangasinierFichisteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MangasinierFichiste.class);
        MangasinierFichiste mangasinierFichiste1 = new MangasinierFichiste();
        mangasinierFichiste1.setId(1L);
        MangasinierFichiste mangasinierFichiste2 = new MangasinierFichiste();
        mangasinierFichiste2.setId(mangasinierFichiste1.getId());
        assertThat(mangasinierFichiste1).isEqualTo(mangasinierFichiste2);
        mangasinierFichiste2.setId(2L);
        assertThat(mangasinierFichiste1).isNotEqualTo(mangasinierFichiste2);
        mangasinierFichiste1.setId(null);
        assertThat(mangasinierFichiste1).isNotEqualTo(mangasinierFichiste2);
    }
}

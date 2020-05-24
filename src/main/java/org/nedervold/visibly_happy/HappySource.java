package org.nedervold.visibly_happy;

public class HappySource {

	public final String postPercents;

	public final String prePercents;

	public HappySource(final String prePercents, final String postPercents) {
		this.prePercents = prePercents;
		this.postPercents = postPercents;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final HappySource other = (HappySource) obj;
		if (postPercents == null) {
			if (other.postPercents != null) {
				return false;
			}
		} else if (!postPercents.equals(other.postPercents)) {
			return false;
		}
		if (prePercents == null) {
			if (other.prePercents != null) {
				return false;
			}
		} else if (!prePercents.equals(other.prePercents)) {
			return false;
		}
		return true;
	}

	public String getSource() {
		return TextUtils.ensureFinalNewline(prePercents) + "%%\n" + TextUtils.ensureFinalNewline(postPercents);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (postPercents == null ? 0 : postPercents.hashCode());
		result = prime * result + (prePercents == null ? 0 : prePercents.hashCode());
		return result;
	}

}

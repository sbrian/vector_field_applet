package vf;

public class EditViewResult
{
	private boolean needsRepaint;
	
	public boolean isBackgroundChanged()
	{
		return backgroundChanged;
	}

	public void setBackgroundChanged(boolean backgroundChanged)
	{
		this.backgroundChanged = backgroundChanged;
	}

	public boolean isResetIntegralXAndY()
	{
		return resetIntegralXAndY;
	}

	public void setResetIntegralXAndY(boolean resetIntegralXAndY)
	{
		this.resetIntegralXAndY = resetIntegralXAndY;
	}

	private boolean backgroundChanged;

	private boolean resetIntegralXAndY;
	
	public void setNeedsRepaint(boolean needsRepaint)
	{
		this.needsRepaint = needsRepaint;
	}

	public boolean isNeedsRepaint()
	{
		return needsRepaint;
	}
}

package srir.frontend.views.compile

import io.udash.bootstrap.UdashBootstrap.ComponentId
import io.udash.bootstrap.button.{ButtonStyle, UdashButton}
import io.udash.bootstrap.form.{UdashForm, UdashInputGroup}
import io.udash.bootstrap.panel.UdashPanel
import io.udash.css._
import io.udash.{ModelProperty, _}
import org.scalajs.dom.File

class CompileView(model: ModelProperty[CompileModel], presenter: CompilePresenter) extends FinalView with CssView {

  import scalatags.JsDom.all._

  private val submitButton = UdashButton(
    buttonStyle = ButtonStyle.Primary,
    block = true, componentId = ComponentId("send")
  )(span("Send file"), tpe := "submit")

  private val msgForm = div(
    UdashForm(
      _ => {
        presenter.processFile()
        true
      }
    )(
      componentId = ComponentId("msg-from"),

      UdashInputGroup()(
        UdashInputGroup.buttons(submitButton.render)
      ).render

    ).render
  )

  private val fileChooser = UdashForm.fileInput()(
    "Select files")("files",
    acceptMultipleFiles = Property(false),
    selectedFiles = model.subSeq(_.selectedFile)
  )


  override def getTemplate: Modifier = div(

    fileChooser,
    showIfElse(model.subSeq(_.selectedFile).transform((s: Seq[File]) => s.isEmpty))(
      i("No files were selected.").render,
      div(msgForm.render,
        UdashPanel()(
          UdashPanel.heading(
            "Compile state:",
          ),
          UdashPanel.body(
            ul(
              li("Stage 1: Compilation => ",
                produce(model.subProp(_.compilerMessage)) {
                  message => span(message).render
                }),
              li("Stage 2: Execution => ",
                produce(model.subProp(_.executionMessage)) {
                  message => span(message).render
                }),
              li("Stage 3: Comparison => ",
                produce(model.subProp(_.comparisonMessage)) {
                  message => span(message).render
                }),
            )
          )
        ).render,
      ).render
    )
  )
}
